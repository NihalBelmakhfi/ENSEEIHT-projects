(*Module de la passe de typage*)
(* doit être conforme à l'interface Passe *)
open Tds
open Exceptions
open Ast
open Type

type t1 = Ast.AstTds.programme
type t2 = Ast.AstType.programme

(* ---------------------------------------------------------------------------------------*)

(* analyse_type_affectable : AstTds.affectable -> AstType.affectable *)
(* Paramètre a : l'affectable à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'affectable
en une affectable de type AstType.affectable *)
(* Erreur si mauvaise utilisation des identifiants *)
let rec analyse_type_affectable a = 
  match a with
    (* Traitement des identifiants *)
    |AstTds.Ident s -> (AstTds.Ident s, 
        let info = info_ast_to_info s  in match info with
          |InfoConst(_ , _) -> Int
          |InfoVar(_, typ, _, _) -> typ
          |InfoFun(_, typ, _) -> typ
        )
    (* Traitement des pointeurs *)
    |AstTds.Dereferencement aff -> let valr,typep= analyse_type_affectable aff in
                                    (match typep with
                                      |Pointeur p -> AstTds.Dereferencement(valr) ,p
                                      |_ -> raise (TypePointeurExpected typep))

(* ---------------------------------------------------------------------------------------*)

(*Fonctions auxilières d'analyse des unaires*)
(*analyse_unaire: AstSyntax.Unaire -> AstType.Unaire*)
(*Paramètre u: Unaire à transformer*)
let analyse_unaire u = 
  match u with
    |AstSyntax.Numerateur -> AstType.Numerateur
    |AstSyntax.Denominateur -> AstType.Denominateur

(* ---------------------------------------------------------------------------------------*)

(*Fonctions auxilières d'analyse des binaires*)
(*analyse_binaire: AstSyntax.Binaire -> AstType.Binaire*)
(*Paramètre b: opérateur binaire à transformer*)
(*Paramètre exp1: premier operande de l'opérateur binaire*)
(*Paramètre exp2: deuxième operande de l'opérateur binaire*)
let analyse_binaire b exp1 exp2=
  match (b, exp1, exp2) with
    |(AstSyntax.Fraction, Int , Int ) -> (AstType.Fraction, Rat)
    |(AstSyntax.Plus, Int , Int ) -> (AstType.PlusInt, Int)
    |(AstSyntax.Plus, Rat , Rat ) -> (AstType.PlusRat, Rat)
    |(AstSyntax.Mult, Int , Int ) -> (AstType.MultInt, Int)
    |(AstSyntax.Mult, Rat , Rat ) -> (AstType.MultRat, Rat)
    |(AstSyntax.Equ, Int , Int ) -> (AstType.EquInt, Bool)
    |(AstSyntax.Equ, Bool , Bool ) -> (AstType.EquBool, Bool)
    |(AstSyntax.Inf, Int, Int) -> (AstType.Inf, Bool)
    |(_, _, _) -> raise (TypeBinaireInattendu (b,exp1,exp2))

(* ---------------------------------------------------------------------------------------*)

(* analyse_type_expression : type -> AstSyntax.expression -> AstType.expression *)
(* Paramètre type : la table des symboles courante *)
(* Paramètre e : l'expression à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'expression
en une expression de type AstType.expression *)
(* Erreur si mauvaise utilisation des identifiants *)

(*On prends une expression qui est de type AstSyntaxe et on renvoie un AstType et son type*)
let rec analyse_type_expression e =
  match e with
    |AstTds.Booleen a -> (AstType.Booleen a, Bool)

    |AstTds.Entier b -> (AstType.Entier b, Int)

    |AstTds.Unaire (u,exp) -> let (expr,exprtype) = (analyse_type_expression exp)
                              in 
                              if (est_compatible exprtype Rat) then
                                ((AstType.Unaire (analyse_unaire u , expr)), Int)
                              else 
                                raise (TypeInattendu(exprtype,Rat))

    |AstTds.Binaire (b, exp1, exp2) -> let (expr1,exprtype1) = (analyse_type_expression exp1) in
                                       let (expr2,exprtype2) = (analyse_type_expression exp2) in
                                       let (operateur, typeretour) = (analyse_binaire b exprtype1 exprtype2) in
                                       (AstType.Binaire (operateur, expr1, expr2) , typeretour)

    |AstTds.Affectable s -> let a, b = analyse_type_affectable s in (AstType.Affectable a,b)

    |AstTds.AppelFonction (s,expList) -> let (exptrans,exptype) = List.split(List.map analyse_type_expression expList) in
                                         (match info_ast_to_info s with
                                            |InfoFun(_,t1,t2) -> 
                                              if est_compatible_list t2 exptype then
                                                AstType.AppelFonction(s,exptrans), t1
                                              else
                                                raise(TypesParametresInattendus(exptype,t2))
                                            | _ -> failwith "Erreur Interne"
                                         )
    
    |Null -> AstType.Null, Pointeur Undefined

    |New (tp) -> AstType.New (tp), Pointeur (tp)

    |Adresse (s) -> let info = info_ast_to_info s  in 
                    let types = (match info with
                                  |InfoConst(_ , _) -> Int
                                  |InfoVar(_, typ, _, _) -> typ
                                  |InfoFun(_, _, _) -> failwith "Erreur fonction")
                    in AstType.Adresse(s), Pointeur types

    |ConditionnelleTer(cond,v,f) -> let cond1, tc = analyse_type_expression cond in
                                    let v1, tv = analyse_type_expression v in
                                    let f1, tf = analyse_type_expression f in
                                    if (est_compatible tc Bool) then
                                      if (est_compatible tv tf) then
                                        AstType.ConditionnelleTer(cond1,v1,f1), tf
                                      else
                                        raise (TypeInattendu(tv,tf))
                                    else
                                      raise (TypeInattendu(Bool,tc))

(* ---------------------------------------------------------------------------------------*)                                         

(* analyse_type_instruction : type -> info_ast option -> AstTds.instruction -> AstType.instruction *)
(* Paramètre type : la table des symboles courante *)
(* Paramètre oia : None si l'instruction i est dans le bloc principal,
                   Some ia où ia est l'information associée à la fonction dans laquelle est l'instruction i sinon *)
(* Paramètre i : l'instruction à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'instruction
en une instruction de type AstType.instruction *)
(* Erreur si mauvaise utilisation des identifiants *)
let rec analyse_type_instruction i =
  match i with
  | AstTds.Declaration (t, n, e) -> let (anl, typeanl) = analyse_type_expression e 
        in 
          modifier_type_variable t n ;
        if (est_compatible t typeanl) then( 
          AstType.Declaration (n, anl))
        else 
          raise (TypeInattendu(typeanl, t))

  | AstTds.Affectation (n,e) -> 
        let (anl1, typeanl1) = analyse_type_affectable n in
        let (anl2, typeanl2) = analyse_type_expression e in 
        if est_compatible typeanl1 typeanl2 then
          AstType.Affectation (anl1, anl2)
        else
          raise(TypeInattendu(typeanl2,typeanl1))


  | AstTds.Affichage e ->  let (anl, typeanl) = analyse_type_expression e in 
        (match typeanl with 
          |Int -> AstType.AffichageInt(anl)
          |Rat -> AstType.AffichageRat(anl)
          |Bool -> AstType.AffichageBool(anl)
          | _ -> failwith("Error: No such type"))

  | AstTds.Conditionnelle (e,b1,b2) -> 
          let (anl,typeanl) = analyse_type_expression e in 
          if est_compatible Bool typeanl then
            AstType.Conditionnelle (anl,
            List.map (fun i -> analyse_type_instruction i) b1,
            List.map (fun i -> analyse_type_instruction i) b2)
          else 
            raise(TypeInattendu(typeanl,Bool))

  | AstTds.ConditionnelleOpt (e,b1) -> 
          let (anl,typeanl) = analyse_type_expression e in 
          if est_compatible Bool typeanl then
            AstType.ConditionnelleOpt (anl,
            List.map (fun i -> analyse_type_instruction i) b1)
          else 
            raise(TypeInattendu(typeanl,Bool))

  | AstTds.TantQue (e,b) -> 
          let (anl, typeanl) = analyse_type_expression e in
          if est_compatible Bool typeanl then
            AstType.TantQue (anl,
            analyse_type_bloc b)
          else
            raise(TypeInattendu(typeanl,Bool))

  | AstTds.Retour (e, n) -> let (anl, anltype) = analyse_type_expression e 
          in 
          let ntype = (match (info_ast_to_info n)with
                        |InfoVar(_,typevar,_,_) -> typevar
                        |InfoFun(_,typefun,_) -> typefun
                        |InfoConst(_,_) -> Int) in
          if (est_compatible anltype ntype) then
            AstType.Retour (anl, n)
          else
            raise (TypeInattendu(anltype,ntype))

  | AstTds.Empty -> AstType.Empty 

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_bloc : tds -> info_ast option -> AstTds.bloc -> AstType.bloc *)
(* Paramètre li : liste d'instructions à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme le bloc en un bloc de type AstType.bloc *)
(* Erreur si mauvaise utilisation des identifiants *)
and analyse_type_bloc li =
  (* Analyse des instructions du bloc avec la tds du nouveau bloc.
     Cette tds est modifiée par effet de bord *)
   let nli = List.map (analyse_type_instruction) li in
   (* afficher_locale tdsbloc ; *) (* décommenter pour afficher la table locale *)
   nli

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_fonction : tds -> AstTds.fonction -> AstType.fonction *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre : la fonction à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme la fonction
en une fonction de type AstType.fonction *)
(* Erreur si mauvaise utilisation des identifiants *)

(*type fonction = Fonction of typ * Tds.info_ast * (typ * Tds.info_ast ) list * bloc*)
(*type fonction = Fonction of Tds.info_ast * Tds.info_ast list * bloc*)
let analyse_type_fonction (AstTds.Fonction(t,n,lp,li))  = 
    let listetypes, listeinfo = List.split lp in
    modifier_type_fonction t listetypes n ;
    let _ = List.map2 modifier_type_variable listetypes listeinfo in
    let newli = analyse_type_bloc li in
    AstType.Fonction(n,listeinfo,newli)

(* ---------------------------------------------------------------------------------------*)

(* analyser : AstTds.programme -> AstType.programme *)
(* Paramètre : le programme à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme le programme
en un programme de type AstType.programme *)
(* Erreur si mauvaise utilisation des identifiants *)
let analyser (AstTds.Programme (fonctions,prog)) =
  let nf = List.map analyse_type_fonction fonctions in
  let nb = analyse_type_bloc prog in
  AstType.Programme (nf,nb)
