
-- Définition de structures de données associatives sous forme d'une liste
-- chaînée associative (LCA).
generic
	type T_Cle is private;
	type T_Donnee is private;
package LCA is

	type T_LCA is private;

	--Couple Clé-Donnée
	type T_CD is 
		record
			Cle    : T_Cle;
			Donnee : T_Donnee;
		end record;
	

	-- Initialiser une Sda.  La Sda est vide.
	procedure Initialiser(Sda: out T_LCA) with
		Post => Est_Vide (Sda);


	-- Est-ce qu'une Sda est vide ?
	function Est_Vide (Sda : T_LCA) return Boolean;


	-- Obtenir le nombre d'éléments d'une Sda. 
	function Taille (Sda : in T_LCA) return Integer with
		Post => Taille'Result >= 0
			and (Taille'Result = 0) = Est_Vide (Sda);


	-- Enregistrer une Donnée associée à une Clé dans une Sda.
	-- Si la clé est déjà présente dans la Sda, sa donnée est changée.
	procedure Enregistrer (Sda : in out T_LCA ; Cle : in T_Cle ; Donnee : in T_Donnee) with
		Post => Cle_Presente (Sda, Cle) and (La_Donnee (Sda, Cle) = Donnee)  
				and (not (Cle_Presente (Sda, Cle)'Old) or Taille (Sda) = Taille (Sda)'Old)
				and (Cle_Presente (Sda, Cle)'Old or Taille (Sda) = Taille (Sda)'Old + 1);

	-- Supprimer la Donnée associée à une Clé dans une Sda.
	-- Exception : Cle_Absente_Exception si Clé n'est pas utilisée dans la Sda
	procedure Supprimer (Sda : in out T_LCA ; Cle : in T_Cle) with
		Post =>  Taille (Sda) = Taille (Sda)'Old - 1; -- un élément de moins      


	-- Savoir si une Clé est présente dans une Sda.
	function Cle_Presente (Sda : in T_LCA ; Cle : in T_Cle) return Boolean;


	-- Obtenir la donnée associée à une Cle dans la Sda.
	-- Exception : Cle_Absente_Exception si Clé n'est pas utilisée dans l'Sda
	function La_Donnee (Sda : in T_LCA ; Cle : in T_Cle) return T_Donnee;
	
	-- Obtenir le couple Clé Donnée de la Sda
	-- Exception : EmptyList_Exception si Clé n'est pas utilisée dans l'Sda
	function La_Paire (Sda : in T_LCA) return T_CD ;
	
	-- Obtenir la cellule suivante de la Sda
	-- Exception : EmptyList_Exception si Clé n'est pas utilisée dans l'Sda
	function La_Cell_Next (Sda : in T_LCA) return T_LCA;


	-- Supprimer tous les éléments d'une Sda.
	procedure Vider (Sda : in out T_LCA) with
		Post => Est_Vide (Sda);

	--Enregistre la donnée dans la Sda sur une position dépendant de la valeur de sa clé
	generic 
		with function min (Gauche, Droite : in T_Cle) return Boolean;
	procedure Enregistrer_Trie (Sda : in out T_LCA ; Cle : in T_Cle ; Donnee : in T_Donnee) ;
	
	
	-- Appliquer un traitement (Traiter) pour chaque couple d'une Sda.
	generic
		with procedure Traiter (Cle : in T_Cle; Donnee: in T_Donnee);
	procedure Pour_Chaque (Sda : in T_LCA);

	
	-- Obtenir le couple clé-donnée avec la clé minimum de la Sda.
	-- Exception : EmptyList_Exception si Clé n'est pas utilisée dans l'Sda
	function Extraire_Min(Sda : in out T_LCA) return T_CD;
	
	

private
	type T_Cellule;
	type T_LCA is access T_Cellule;
		
	type T_Cellule is 
		record
			Paire : T_CD;
			Next   : T_LCA;
		end record;
		

end LCA;
