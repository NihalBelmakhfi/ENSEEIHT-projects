
(*Module de la passe gestion de mémoire*)
(* doit être conforme à l'interface Passe *)
open Tds
open Ast
open Type

type t1 = Ast.AstType.programme
type t2 = Ast.AstPlacement.programme

(* ---------------------------------------------------------------------------------------*)

(* analyse_placement_instruction : Int -> Registre -> AstType.instruction -> AstPlacement.instruction -> Int *)
(* i : instruction à placer en mémoire *)
(* base : indice de placement mémoire actuel *)
(* registre : registre dans lequel on place en mémoire *)
(* analyse_placement_instruction retourne l'indice de placement mémoire de l'instruction pour la pile*)
let rec analyse_placement_instruction i base registre =

  (* Définition d'une fonction auxilière qui permet de calculer la taille dans la mémore d'une liste*)
  let rec taille_liste maliste = match maliste with
    |[] -> 0
    |t::q -> getTaille t + taille_liste q
  in

  match i with
  | AstType.Affectation (a,b)-> AstPlacement.Affectation (a,b),0

  | AstType.Declaration (info, n) ->
        (match info_ast_to_info info with (*Récupère l'information associée à un noeud*)
          |InfoVar(_,t,_,_) -> modifier_adresse_variable base registre info; 
          AstPlacement.Declaration (info, n),(getTaille t)
          |_ -> failwith "nthg")

  | Conditionnelle(a, t, e) -> 
        let t1 = analyse_placement_bloc base registre t in
        let e1 = analyse_placement_bloc base registre e in
        AstPlacement.Conditionnelle (a, t1, e1),0

  | ConditionnelleOpt(a, t) -> 
        let t1 = analyse_placement_bloc base registre t in
        AstPlacement.ConditionnelleOpt (a, t1),0

  | AstType.TantQue (a, b) ->
        let b = analyse_placement_bloc base registre b in
        AstPlacement.TantQue(a,b),0
  
  | AffichageInt (exp) -> AstPlacement.AffichageInt (exp), 0

  | AffichageRat (exp) -> AstPlacement.AffichageRat (exp), 0

  | AffichageBool (exp)-> AstPlacement.AffichageBool (exp), 0

  | Retour (exp,info) -> (match info_ast_to_info info with (*Récupère l'information associée à un noeud*)
                            |InfoFun(_,t,tlist) -> AstPlacement.Retour (exp, getTaille t, taille_liste tlist), 0
                            |_ -> failwith "Erreur Retour")

  | Empty -> AstPlacement.Empty, 0
  
(* ---------------------------------------------------------------------------------------*)
(* analyse_placement_bloc : Int -> Registre -> AstType.bloc -> AstPlacement.bloc *)
(* inst : liste d'instructions à placer en mémoire *)
(* base : indice de placement mémoire actuel *)
(* registre : registre dans lequel on place en mémoire *)
(* Vérifie la bonne utilisation des identifiants et tranforme le bloc en un bloc de type AstPlacement.bloc *)

  and analyse_placement_bloc base registre inst =(* analyse_placement_bloc : AstType.bloc -> unit, il place en mémoir le bloc*)
    let rec funAux base inst =
    (match inst with
      |head::tail ->
        let instruction, taille = analyse_placement_instruction head base registre in
        let liInst, taille2 = funAux (base + taille) tail in instruction::liInst, taille2
      | [] -> [], base
    )in
    let inst, newtaille = funAux base inst in inst, newtaille - base

(* ---------------------------------------------------------------------------------------*)

(* analyse_placement_fonction : AstType.fonction -> AstPlacement.fonction *)
(* analyse_placement_fonction place une fonction en mémoire *)
let rec analyse_placement_fonction (AstType.Fonction(info, infos,bloc)) = 
  let _ = analyse_placement_param infos in
  let newbloc = analyse_placement_bloc 3 "LB" bloc in
  AstPlacement.Fonction(info,infos,newbloc)

    and analyse_placement_param infos = 
    ignore (
        List.fold_right (fun info base -> 
            match info_ast_to_info info with 
            |InfoVar(_,t,_,_) -> let nbase = base - Type.getTaille t in 
                                modifier_adresse_variable nbase "LB" info ; 
                                nbase
            | _ -> assert false 
            )
        infos 0
    )

(* ---------------------------------------------------------------------------------------*)

(* AstType.Programme -> AstPlacement.Programme *)
(* AstType.Programme effectue le traitement du placement mémoire du programme*)
let analyser(AstType.Programme(fonctions, programme))=
  let f = List.map analyse_placement_fonction fonctions in
  let programme = analyse_placement_bloc 0 "SB" programme in
  AstPlacement.Programme(f, programme)
