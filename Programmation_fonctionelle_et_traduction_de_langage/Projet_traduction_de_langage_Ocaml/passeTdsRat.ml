(* Module de la passe de gestion des identifiants *)
(* doit être conforme à l'interface Passe *)
open Tds
open Exceptions
open Ast

type t1 = Ast.AstSyntax.programme
type t2 = Ast.AstTds.programme

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_affectable : tds -> AstSyntax.affectable -> AstTds.affectable *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre a : l'affectable à analyser *)
(* Vérifie la bonne utilisation des identifiants et transforme l'affectable
en un affectable de type AstTds.affectable *)
(* Erreur si mauvaise utilisation des identifiants *)
let rec analyse_tds_affectable tds a = match a with
    |AstSyntax.Ident s -> (let inform = (match (Tds.chercherGlobalement tds s) with
                |None -> raise (IdentifiantNonDeclare s)
                |Some inform -> inform ) in 
                (match (info_ast_to_info inform) with
                |InfoVar(_,_,_,_) -> AstTds.Ident(inform)
                |InfoConst(_,_) -> AstTds.Ident(inform)
                |_ -> raise (MauvaiseUtilisationIdentifiant s)))
    |AstSyntax.Dereferencement a -> AstTds.Dereferencement(analyse_tds_affectable tds a)

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_expression : tds -> AstSyntax.expression -> AstTds.expression *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre e : l'expression à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'expression
en une expression de type AstTds.expression *)
(* Erreur si mauvaise utilisation des identifiants *)

(*On prends une expression qui est de type AstSyntaxe et on renvoie un AstTds*)
let rec analyse_tds_expression tds e =
  match e with
    |AstSyntax.Booleen a -> AstTds.Booleen a

    |AstSyntax.Entier b -> AstTds.Entier b

    |AstSyntax.Unaire (u,exp) -> AstTds.Unaire (u , (analyse_tds_expression tds exp))

    |AstSyntax.Binaire (b, exp1, exp2) -> AstTds.Binaire (b, (analyse_tds_expression tds exp1), (analyse_tds_expression tds exp2))

    |AstSyntax.Affectable a -> AstTds.Affectable (analyse_tds_affectable tds a)

    |AstSyntax.AppelFonction (s,expList) -> begin match (chercherGlobalement tds s) with
                                              |None -> raise (IdentifiantNonDeclare s)
                                              |Some inform -> (match info_ast_to_info inform with
                                                                |InfoFun(_,_,_) -> AstTds.AppelFonction(inform,
                                                                  List.map(fun elt -> analyse_tds_expression tds elt) expList)
                                                                | _ -> raise (MauvaiseUtilisationIdentifiant s))
                                            end
    |AstSyntax.Null -> AstTds.Null

    |AstSyntax.New (tp) -> AstTds.New (tp)

    |AstSyntax.Adresse (s) -> (let inform = (match (Tds.chercherGlobalement tds s) with
                                    |None -> raise (IdentifiantNonDeclare s)
                                    |Some inform -> inform ) in 
                                  (match (info_ast_to_info inform) with
                                    |InfoVar(_,_,_,_) -> AstTds.Adresse(inform)
                                    |InfoConst(_,_) -> AstTds.Adresse(inform)
                                    |_ -> raise (MauvaiseUtilisationIdentifiant s)))

    |AstSyntax.ConditionnelleTer (e1, e2, e3) -> let anl1 = analyse_tds_expression tds e1 in
                                       let anl2 = analyse_tds_expression tds e2 in
                                       let anl3 = analyse_tds_expression tds e3 in
                                       AstTds.ConditionnelleTer(anl1,anl2,anl3)
    
(* ---------------------------------------------------------------------------------------*)

(* Conversion des affectables*)
(* fonction prise de printerAst.ml *)
(* string_of_affectable: Affectable -> String*)
(* paramètre a : affectable a transformer en String*)
  let rec string_of_affectable a =
    match a with
      |AstSyntax.Ident s -> s
      |AstSyntax.Dereferencement p -> string_of_affectable p

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_instruction : tds -> info_ast option -> AstSyntax.instruction -> AstTds.instruction *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre oia : None si l'instruction i est dans le bloc principal,
                   Some ia où ia est l'information associée à la fonction dans laquelle est l'instruction i sinon *)
(* Paramètre i : l'instruction à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'instruction
en une instruction de type AstTds.instruction *)
(* Erreur si mauvaise utilisation des identifiants *)
let rec analyse_tds_instruction tds oia i =
  match i with
  | AstSyntax.Declaration (t, n, e) ->
      begin
        match chercherLocalement tds n with
        | None ->
            (* L'identifiant n'est pas trouvé dans la tds locale,
            il n'a donc pas été déclaré dans le bloc courant *)
            (* Vérification de la bonne utilisation des identifiants dans l'expression *)
            (* et obtention de l'expression transformée *)
            let ne = analyse_tds_expression tds e in
            (* Création de l'information associée à l'identfiant *)
            let info = InfoVar (n,Undefined, 0, "") in
            (* Création du pointeur sur l'information *)
            let ia = info_to_info_ast info in
            (* Ajout de l'information (pointeur) dans la tds *)
            ajouter tds n ia;
            (* Renvoie de la nouvelle déclaration où le nom a été remplacé par l'information
            et l'expression remplacée par l'expression issue de l'analyse *)
            AstTds.Declaration (t, ia, ne)
        | Some _ ->
            (* L'identifiant est trouvé dans la tds locale,
            il a donc déjà été déclaré dans le bloc courant *)
            raise (DoubleDeclaration n)
      end
  
  | AstSyntax.Affectation (a,e) ->
      begin
        let n = string_of_affectable a in
        match chercherGlobalement tds n with
        | None ->
          (* L'identifiant n'est pas trouvé dans la tds globale. *)
          raise (IdentifiantNonDeclare n)
        | Some info ->
          (* L'identifiant est trouvé dans la tds globale,
          il a donc déjà été déclaré. L'information associée est récupérée. *)
          begin
            match info_ast_to_info info with
            | InfoVar _ ->
              (* Vérification de la bonne utilisation des identifiants dans l'expression *)
              (* et obtention de l'expression transformée *)
              let ne = analyse_tds_expression tds e in
              (* Renvoie de la nouvelle affectation où le nom a été remplacé par l'information
                 et l'expression remplacée par l'expression issue de l'analyse *)
              AstTds.Affectation (AstTds.Ident(info), ne)
            |  _ ->
              (* Modification d'une constante ou d'une fonction *)
              raise (MauvaiseUtilisationIdentifiant n)
          end
      end
  
  | AstSyntax.Constante (n,v) ->
      begin
        match chercherLocalement tds n with
        | None ->
          (* L'identifiant n'est pas trouvé dans la tds locale,
             il n'a donc pas été déclaré dans le bloc courant *)
          (* Ajout dans la tds de la constante *)
          ajouter tds n (info_to_info_ast (InfoConst (n,v)));
          (* Suppression du noeud de déclaration des constantes devenu inutile *)
          AstTds.Empty
        | Some _ ->
          (* L'identifiant est trouvé dans la tds locale,
          il a donc déjà été déclaré dans le bloc courant *)
          raise (DoubleDeclaration n)
      end

  | AstSyntax.Affichage e ->
      (* Vérification de la bonne utilisation des identifiants dans l'expression *)
      (* et obtention de l'expression transformée *)
      let ne = analyse_tds_expression tds e in
      (* Renvoie du nouvel affichage où l'expression remplacée par l'expression issue de l'analyse *)
      AstTds.Affichage (ne)

  | AstSyntax.Conditionnelle (c,t,e) ->
      (* Analyse de la condition *)
      let nc = analyse_tds_expression tds c in
      (* Analyse du bloc then *)
      let tast = analyse_tds_bloc tds oia t in
      (* Analyse du bloc else *)
      let east = analyse_tds_bloc tds oia e in
      (* Renvoie la nouvelle structure de la conditionnelle *)
      AstTds.Conditionnelle (nc, tast, east)

  | AstSyntax.ConditionnelleOpt (c,t) ->
      (* Analyse de la condition *)
      let nc = analyse_tds_expression tds c in
      (* Analyse du bloc then *)
      let tast = analyse_tds_bloc tds oia t in
      (* Renvoie la nouvelle structure de la conditionnelle *)
      AstTds.ConditionnelleOpt (nc, tast)

  | AstSyntax.TantQue (c,b) ->
      (* Analyse de la condition *)
      let nc = analyse_tds_expression tds c in
      (* Analyse du bloc *)
      let bast = analyse_tds_bloc tds oia b in
      (* Renvoie la nouvelle structure de la boucle *)
      AstTds.TantQue (nc, bast)

  | AstSyntax.Retour (e) ->
      begin
      (* On récupère l'information associée à la fonction à laquelle le return est associée *)
      match oia with
        (* Il n'y a pas d'information -> l'instruction est dans le bloc principal : erreur *)
      | None -> raise RetourDansMain
        (* Il y a une information -> l'instruction est dans une fonction *)
      | Some ia ->
        (* Analyse de l'expression *)
        let ne = analyse_tds_expression tds e in
        AstTds.Retour (ne,ia)
      end

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_bloc : tds -> info_ast option -> AstSyntax.bloc -> AstTds.bloc *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre oia : None si le bloc li est dans le programme principal,
                   Some ia où ia est l'information associée à la fonction dans laquelle est le bloc li sinon *)
(* Paramètre li : liste d'instructions à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme le bloc en un bloc de type AstTds.bloc *)
(* Erreur si mauvaise utilisation des identifiants *)
and analyse_tds_bloc tds oia li =
  (* Entrée dans un nouveau bloc, donc création d'une nouvelle tds locale
  pointant sur la table du bloc parent *)
  let tdsbloc = creerTDSFille tds in
  (* Analyse des instructions du bloc avec la tds du nouveau bloc.
     Cette tds est modifiée par effet de bord *)
   let nli = List.map (analyse_tds_instruction tdsbloc oia) li in
   (* afficher_locale tdsbloc ; *) (* décommenter pour afficher la table locale *)
   nli

(* ---------------------------------------------------------------------------------------*)

(* analyse_tds_fonction : tds -> AstSyntax.fonction -> AstTds.fonction *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre : la fonction à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme la fonction
en une fonction de type AstTds.fonction *)
(* Erreur si mauvaise utilisation des identifiants *)
(* A savoir :*)
(*type fonction en AstSyntax = Fonction of typ * string * (typ * string) list * bloc*)
(*type fonction en AstTds    = Fonction of typ * Tds.info_ast * (typ * Tds.info_ast ) list * bloc*)
let analyse_tds_fonction maintds (AstSyntax.Fonction(t,n,lp,li))  = 
      match (chercherGlobalement maintds n) with
        (*Si la fonction n'existe pas sur la table des symboles, on la définit et ses paramètres*)
        |None ->
            let monTds = creerTDSFille maintds in
            let funAux monTds (t,id) =
              begin match chercherLocalement monTds id with
                |Some _ -> raise(DoubleDeclaration id)
                |None -> let l = info_to_info_ast (InfoVar(id,t,0,""))
                  in ajouter monTds id l ;
                  (t,l)
              end
            in
            let newt = t in
            let newlp = List.map(funAux monTds) lp in
            let listetype,_ = List.split lp in
            let newn = info_to_info_ast (InfoFun(n,Undefined,listetype)) 
            in ajouter maintds n newn ;
            let newli = analyse_tds_bloc monTds (Some newn) li in
            AstTds.Fonction(newt,newn,newlp,newli)

        |Some inform -> 
          match (info_ast_to_info inform) with
            |InfoFun(_,_,liste) -> 
              if (List.length liste = List.length lp)
                then raise (DoubleDeclaration n)
              else
                (*Bloc qui se répète pour définir une fonction*)
                let monTds = creerTDSFille maintds in
                let funAux monTds (t,id) =
                  begin
                    match chercherLocalement monTds id with
                  |Some _ -> raise(DoubleDeclaration id)
                  |None -> let l = info_to_info_ast (InfoVar(id,t,0,""))
                    in ajouter monTds id l ;
                    (t,l)
                  end
                in
                let newt = t in
                let newlp = List.map(funAux monTds) lp in
                let listetype,_ = List.split lp in
                let newn = info_to_info_ast (InfoFun(n,Undefined,listetype)) 
                in ajouter maintds n newn ;
                let newli = analyse_tds_bloc monTds (Some newn) li in
                AstTds.Fonction(newt,newn,newlp,newli)
                (*Fin du bloc qui se répète*)
            | _ -> raise (MauvaiseUtilisationIdentifiant n)

(* ---------------------------------------------------------------------------------------*)

(* analyser : AstSyntax.programme -> AstTds.programme *)
(* Paramètre : le programme à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme le programme
en un programme de type AstTds.programme *)
(* Erreur si mauvaise utilisation des identifiants *)
let analyser (AstSyntax.Programme (fonctions,prog)) =
  let tds = creerTDSMere () in
  let nf = List.map (analyse_tds_fonction tds) fonctions in
  let nb = analyse_tds_bloc tds None prog in
  AstTds.Programme (nf,nb)
