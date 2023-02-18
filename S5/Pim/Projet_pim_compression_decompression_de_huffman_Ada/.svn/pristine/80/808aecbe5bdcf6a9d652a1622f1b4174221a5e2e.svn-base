
-- Définition de structures de données associatives sous forme d'un arbre
-- binaire de recherche (ABR_HUFFMAN).
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with LCA;
generic
	type T_Cle is private;
	type T_Donnee is private;
	with function "<" (Gauche, Droite : in T_Cle) return Boolean;

package ABR_HUFFMAN is

	type T_ABR is private;
	
	package LCA_Char_String is
		new LCA (T_Cle => T_Donnee, T_Donnee => Unbounded_String);
    use LCA_Char_String;
       
      package LCA_Integer_String is
		new LCA (T_Cle => Integer, T_Donnee => T_Donnee);

	-- Initialiser Un Abr.  La Abr est vide.
	procedure Initialiser(Abr: out T_ABR) with
		Post => Est_Vide (Abr);


	-- Est-ce qu'Un Abr est vide ?
	function Est_Vide (Abr : T_ABR) return Boolean;


	-- Obtenir le nombre d'éléments d'Un Abr. 
	function Taille (Abr : in T_ABR) return Integer with
		Post => Taille'Result >= 0
			and (Taille'Result = 0) = Est_Vide (Abr);
			
	-- Savoir si Une Clé est présente dans Un Abr.
	function Cle_Presente (Abr : in T_ABR ; Cle : in T_Cle) return Boolean;


	-- Enregistrer Une Donnée associée à Un Clé dans Un Abr.
	-- Si la clé est déjà présente dans la Abr, sa donnée est changée.
	procedure Enregistrer_Cle_Donnee (Abr : in out T_ABR ; Cle : in T_Cle ; Donnee : in T_Donnee) with
		Post => (Taille (Abr) = Taille (Abr)'Old or Taille (Abr) = Taille (Abr)'Old + 1);
			
	-- Enregistrer dans un Abr un sous arbre gauche et un sous arbre droit.
	procedure Enregistrer_abrg_abrd (Abr : in out T_ABR ; Sag : in T_ABR ; Sad : in T_ABR) with
          Post => (Taille (Abr) = Taille (Abr)'Old + Taille(Sag) + Taille(Sad));
		  
	--Enregistrer ou modifier la Clé du sommet de l'Abr
	procedure Enregistrer_Cle(Abr : in out T_ABR ; Cle : in T_Cle );
    
	-- Supprimer la Donnée associée à Une Clé dans Un Abr.
	-- Exception : Cle_Absente_Exception si Clé n'est pas utilisée dans la Abr
	procedure Supprimer (Abr : in out T_ABR ; Cle : in T_Cle) with
		Post =>  Taille (Abr) = Taille (Abr)'Old - 1; -- un élément de moins


	-- Obtenir La donnée du sommet de l'Abr
	function La_Donnee (Abr : in T_ABR) return T_Donnee;
	

	-- Supprimer tous les éléments d'Un Abr.
	procedure Vider (Abr : in out T_ABR) with
		Post => Est_Vide (Abr);
		
	--Savoir si un arbre est une feuille ou non	
	function Est_Feuille (Abr : in T_ABR) return Boolean;
	
	--Obtenir le fils gauche d'un Abr
	function SAG (Abr : in T_ABR) return T_ABR;
	
	--Obtenir le fils droit d'un Abr
	function SAD (Abr : in T_ABR) return T_ABR;

	--Afficher l'arbre d'Huffman sous sa forme arborescente
	generic
		with procedure Traiter_Noeud(Cle : in T_Cle; Donnee : in T_Donnee);
	procedure Affichage_Huffman (Abr : in T_ABR ; Suite : in out Unbounded_String);

	--Obtenir dans Suite le code d'huffman d'un Abr
	procedure Code_Huffman(Abr : in T_ABR; Suite : in out Unbounded_String);
	
	--Obtenir dans lca la liste qui à chaque donnée d'une feuille lui associe son code d'Huffman
	procedure Table_Huffman (Abr : in T_ABR; lca : in out LCA_Char_String.T_LCA; Suite : in out Unbounded_String) ;
	
	--Obtenir dans Abr l'arbre d'Huffman correspondant au code D'Huffman fournit dans Suite
	procedure ReConsAbr(Abr : in out T_ABR; n : in out Integer; tab_Char : in LCA_Integer_String.T_LCA; Suite : in out Unbounded_String);


private
	type T_Noeud;
	type T_ABR is access T_Noeud;
	type T_Noeud is
		record
			Cle: T_Cle;
			Donnee : T_Donnee;
			Sous_Arbre_Gauche : T_ABR;
			Sous_Arbre_Droit : T_ABR;
			-- Invariant
			--    Pour tout noeud N dans Sous_Arbre_Gauche, N.Cle < Cle
			--    Pour tout noeud N dans Sous_Arbre_Droit,  N.Cle >= Cle
		end record;

end ABR_HUFFMAN;
