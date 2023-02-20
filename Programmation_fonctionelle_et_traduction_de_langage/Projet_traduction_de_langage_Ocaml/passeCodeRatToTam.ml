(*module PasseCodeRatToTam : Passe.Passe  with type t1 = Ast.AstPlacement.programme and type t2 = string
*)
open Ast
open Tds
open AstPlacement
open Type
open Code
open Tam

type t1 = Ast.AstPlacement.programme
type t2 = string

let n = "\n"

(* ---------------------------------------------------------------------------------------*)

(*analyse_code_affectable: AstTds.affectable -> bool -> String*)
(* Paramètre a : l'affectable à analyser *)
(* Paramètre change : boolean qui nous indique si il faut modifier a ou pas *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'affectable
en String, retourne le code TAM relatif à l'affectable a*)
let rec analyse_code_affectable change a = match a with
  |AstTds.Ident inform -> (match (info_ast_to_info inform) with 
                            |InfoVar(_,t,d,r) -> let taille = (getTaille t) in
                                if change then store taille d r
                                else load taille d r
                            |InfoConst(_,nb) -> loadl_int nb
                            | _ -> failwith "Probleme")
  |AstTds.Dereferencement df -> match df with 
                                  |AstTds.Ident _-> analyse_code_affectable change df
                                  |AstTds.Dereferencement _ -> loadi 1

(* ---------------------------------------------------------------------------------------*)

(* analyse_code_expression : AstType.expression -> String *)
(* paramètre exp: l'expression à transformer en code TAM*)
(* Retourne le code TAM relatif à cette expression *)
let rec analyse_code_expression exp =
  match exp with
    |AstType.Affectable infoast -> analyse_code_affectable false infoast

    |AstType.AppelFonction(info, expl) ->
          begin 
            match info_ast_to_info info with
            | InfoFun(str,_,_) -> (List.fold_left (fun code exp -> code ^ n ^ (analyse_code_expression exp)) "" expl)
            ^ call "SB" str
            |_ -> failwith "Probleme"
          end

    |AstType.Binaire (operateur,exp1,exp2) ->
          (analyse_code_expression exp1) ^ (analyse_code_expression exp2) ^
          begin match operateur with
              |Inf -> subr "ILss" 
              |PlusInt -> subr "IAdd" 
              |PlusRat -> call "LB" "RAdd"
              |MultInt -> subr "IMul"  
              |MultRat -> call "LB" "RMul"
              |EquBool -> subr "IEq" 
              |EquInt -> subr "IEq"
              |Fraction ->  call "LB" "Norm" 
          end

    |AstType.Unaire (Numerateur,exp1) -> analyse_code_expression exp1 ^ pop 0 1

    |AstType.Unaire (Denominateur,exp1) -> analyse_code_expression exp1 ^ pop 1 1

    |AstType.Entier (i) -> loadl_int i

    |AstType.Booleen (b) -> if b then loadl_int 1 else loadl_int 0

    |Adresse (s) -> let info = info_ast_to_info s in 
          (match info with
          |InfoConst(_ , _) -> failwith "Erreur Constante"
          |InfoVar(_, typ, d, r) -> let taille = (getTaille typ) in load taille d r
          |InfoFun(_, _, _) -> failwith "Erreur fonction")
    
    |Null -> loadl_int (-1)

    |New (tp) -> let taille = getTaille tp in (loadl_int taille ^ subr "Malloc")

    |ConditionnelleTer (e1,e2,e3) ->  
          let ne = analyse_code_expression e1 in 
          let nb1= analyse_code_expression e2 in 
          let nb2= analyse_code_expression e3 in 
          let etiq_else = getEtiquette() in
          let etiq_fin_if = getEtiquette() in 
          ne ^
          jumpif 0 etiq_else ^
          nb1 ^
          jump etiq_fin_if ^ 
          label etiq_else ^
          nb2^
          label etiq_fin_if 

(* ---------------------------------------------------------------------------------------*)

(* analyse_code_instruction : AstType.instruction -> String * int *)
(* i : instruction dont on souhaite obtenir le code TAM *)
(* Retournee le code TAM relatif à cette instruction*)
let rec analyse_code_instruction i =
  match i with
  | AstPlacement.Declaration (info, exp) -> 
          begin match info_ast_to_info info with
              |InfoVar(_,t,i,reg) -> push (Type.getTaille t)
                                    ^ (analyse_code_expression exp) 
                                    ^ store (Type.getTaille t) i reg
              |_ -> failwith "Probleme"
          end

  | AstPlacement.Affectation(a, e) -> 
          let analysera = analyse_code_affectable true a in 
          (analyse_code_expression e)^ (analysera)

  | AstPlacement.AffichageInt e -> analyse_code_expression e ^ subr "IOut"

  | AstPlacement.AffichageBool e -> analyse_code_expression e ^ subr "BOut"
  
  | AstPlacement.AffichageRat e -> analyse_code_expression e ^ n ^ call "LB" "rout"
  
  | AstPlacement.Conditionnelle (cond, b1, b2) -> 
          let ne = analyse_code_expression cond in 
          let nb1, pop_taille_if = analyse_code_bloc b1 in 
          let nb2, pop_taille_else = analyse_code_bloc b2 in 
          let etiq_else = getEtiquette() in
          let etiq_fin_if = getEtiquette() in 
          ne ^
          jumpif 0 etiq_else ^
          nb1^ pop 0 pop_taille_if ^
          jump etiq_fin_if ^ 
          label etiq_else ^
          nb2^ pop 0 pop_taille_else ^
          label etiq_fin_if

  | AstPlacement.ConditionnelleOpt (cond, b1) -> 
          let ne = analyse_code_expression cond in 
          let nb1, pop_taille_if = analyse_code_bloc b1 in 
          let etiq_else = getEtiquette() in
          let etiq_fin_if = getEtiquette() in 
          ne ^
          jumpif 0 etiq_else ^
          nb1^ pop 0 pop_taille_if ^
          jump etiq_fin_if ^ 
          label etiq_else ^
          label etiq_fin_if

  | AstPlacement.TantQue(c, b) ->
          let nc = analyse_code_expression c in 
          let nb, pop_taille_tq = analyse_code_bloc b in 
          let etiq = getEtiquette() in
          let etiq2 = getEtiquette() in 
          label etiq ^
          nc ^
          jumpif 0 etiq2 ^
          nb ^
          pop 0 pop_taille_tq ^
          jump etiq ^
          label etiq2

  | AstPlacement.Retour (e, a1, a2) -> analyse_code_expression e ^ return a1 a2

  | AstPlacement.Empty -> n

(* ---------------------------------------------------------------------------------------*)

(* analyse_code_bloc : AstPlacement.bloc -> String*)
(* bloc : la liste d'instructions à transformer en code TAM*)
(* Retournee le code TAM relatif à ce bloc*)
and analyse_code_bloc bloc = 
    let listInst, taille = bloc in
    let code = List.fold_left (fun code i -> let c = analyse_code_instruction i in code^n^c ) "" listInst in
    (push taille) ^ code ^ (pop 0 taille), taille

(* ---------------------------------------------------------------------------------------*)

(* analyse_code_fonction : AstPlacement.fonction -> String *)
(* Retourne le code TAM qui traduit cette fonction *)
let analyse_code_fonction (Fonction(info,_,b))  =
    match info_ast_to_info info with
        |InfoFun(nomfun,_,_) -> let bloc,_ = (analyse_code_bloc b) in
                      label nomfun ^ bloc ^ halt

        |_ -> failwith "prob"

(* ---------------------------------------------------------------------------------------*)

 (* analyse_code_programme : AstPlacement.programme -> String *)
(* Retourne le code TAM qui traduit le programme passé en paramètre *)
let analyser (Programme (fonctions,prog)) =
  let bloc,_ = (analyse_code_bloc prog) in
  Code.getEntete () ^
  List.fold_left (fun code e -> code ^ n ^ (analyse_code_fonction e)) "" fonctions ^
  label "main" ^
  bloc ^
  halt
 


  