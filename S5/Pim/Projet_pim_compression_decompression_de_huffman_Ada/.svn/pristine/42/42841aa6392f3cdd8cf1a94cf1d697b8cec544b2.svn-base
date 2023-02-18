with Sda_Exceptions;         use Sda_Exceptions;
with Ada.Text_IO;            use Ada.Text_IO;
with Ada.Unchecked_Deallocation;

package body ABR_HUFFMAN is

	procedure Free is
		new Ada.Unchecked_Deallocation (Object => T_Noeud, Name => T_ABR);


	procedure Initialiser(Abr: out T_ABR) is
	begin
		Abr := null;
	end Initialiser;


	function Est_Vide (Abr : T_ABR) return Boolean is
	begin
		return Abr = null;
	end Est_Vide;

	-- Est-ce que la Cle est utilisée dans la Abr
	function Cle_Presente (Abr : in T_ABR ; Cle : in T_Cle) return Boolean is
	begin
		if Abr = null then
			return False;
		end if;
		if Abr.all.Cle = Cle then
			return True;
		elsif "<"(Abr.all.Cle, Cle) then
			return Cle_Presente(Abr.all.Sous_Arbre_Gauche,Cle);
		else
			return Cle_Presente(Abr.all.Sous_Arbre_Droit, Cle);
		end if;
	end Cle_Presente;



	function Taille (Abr : in T_ABR) return Integer is
	begin
		if Abr = null then
			return 0;
		else
			return 1  + Taille(Abr.all.Sous_Arbre_Gauche)+ Taille(Abr.all.Sous_Arbre_Droit);
		end if;
	end Taille;


	procedure Enregistrer_Cle_Donnee (Abr : in out T_ABR ; Cle : in T_Cle ; Donnee : in T_Donnee) is
	begin
		if Abr = null then
			Abr := new T_Noeud'(Cle,Donnee,null,null);
		end if;
		if Abr.all.Cle = Cle then
			Abr.all.Donnee := Donnee;
		elsif "<" (Cle, Abr.all.Cle) then
			Enregistrer_Cle_Donnee (Abr.all.Sous_Arbre_Gauche, Cle, Donnee);
		else
			Enregistrer_Cle_Donnee (Abr.all.Sous_Arbre_Droit, Cle, Donnee);
		end if;
	end Enregistrer_Cle_Donnee;

	procedure Enregistrer_Cle(Abr : in out T_ABR ; Cle : in T_Cle ) is
	begin
		if Abr = null then
			Abr := new T_Noeud;
		end if;
		Abr.all.Cle := Cle;
	end Enregistrer_Cle;

	procedure Enregistrer_abrg_abrd (Abr : in out T_ABR; Sag : in T_ABR; Sad : in T_ABR) is
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		Abr.all.Sous_Arbre_Gauche := Sag;
		Abr.all.Sous_Arbre_Droit := Sad;

	end;


	function La_Donnee (Abr : in T_ABR) return T_Donnee is
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		return Abr.all.Donnee;
	end La_Donnee;

	function SAG (Abr : in T_ABR) return T_ABR is
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		return Abr.all.Sous_Arbre_Gauche;
	end SAG;

	function SAD (Abr : in T_ABR) return T_ABR is
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		return Abr.all.Sous_Arbre_Droit;
	end SAD;


	procedure Supprimer (Abr : in out T_ABR ; Cle : in T_Cle) is
	begin
		if Abr = null then
			raise Cle_Absente_Exception;
		end if;
		if Abr.all.Cle = Cle then
			Free(Abr);
		elsif "<"(Cle, Abr.all.Cle) then
			Supprimer (Abr.all.Sous_Arbre_Gauche, Cle);
		else
			Supprimer(Abr.all.Sous_Arbre_Droit, Cle);
		end if;
	end Supprimer;


	procedure Vider (Abr : in out T_ABR) is
	begin
		if Abr /= null then
			Vider(Abr.all.Sous_Arbre_Gauche);
			Vider(Abr.all.Sous_Arbre_Droit);
			Free(Abr);
		end if;
	end Vider;

	function Est_Feuille (Abr : in T_ABR) return Boolean is
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		return Est_Vide(Abr.all.Sous_Arbre_Gauche) and Est_Vide(Abr.all.Sous_Arbre_Droit);
	end Est_Feuille;


	procedure Affichage_Huffman (Abr : in T_ABR ; Suite : in out Unbounded_String) is
		s1 : Unbounded_String;
		s2 : Unbounded_String;
		zero : constant Unbounded_String := To_Unbounded_String("0");
		one : constant Unbounded_String := To_Unbounded_String("1");
		space : Unbounded_String := Null_Unbounded_String;

	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;

		for i in 1..Length(Suite) loop
			space := space & To_Unbounded_String("  |      ");
		end loop;


		if Est_Feuille(Abr) then
			Traiter_Noeud(Abr.all.Cle, Abr.all.Donnee);
				New_Line;
		else
			Traiter_Noeud(Abr.all.Cle, Abr.all.Donnee);
			New_Line;

			s1 := Suite & zero;
			put(""&To_String(Space));
			put("  \--"&To_String(zero)&"--");
			Affichage_Huffman(Abr.all.Sous_Arbre_Gauche, s1);
			s2 := Suite & one;
			put(""&To_String(Space));
			put("  \--"&To_String(one)&"--");
			Affichage_Huffman(Abr.all.Sous_Arbre_Droit, s2);

		end if;

	end Affichage_Huffman;

	procedure Table_Huffman (Abr : in T_ABR; lca : in out LCA_Char_String.T_LCA; Suite : in out Unbounded_String) is
		s1 : Unbounded_String;
		s2 : Unbounded_String;
		zero : constant Unbounded_String := To_Unbounded_String("0");
		one : constant Unbounded_String := To_Unbounded_String("1");
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		if Est_Feuille(Abr) then
			Enregistrer(lca, Abr.all.Donnee, suite);
		else
			s1 := Suite & zero;
			Table_Huffman(Abr.all.Sous_Arbre_Gauche, lca, s1);
			s2 := Suite & one;
			Table_Huffman(Abr.all.Sous_Arbre_Droit, lca, s2);
		end if;

	end Table_Huffman;

	procedure Code_Huffman(Abr : in T_ABR; Suite : in out Unbounded_String) is
		zero : constant Unbounded_String := To_Unbounded_String("0");
		one : constant Unbounded_String := To_Unbounded_String("1");
	begin
		if Abr = null then
			raise Empty_Tree_Exception;
		end if;
		if Est_Feuille(Abr) then
			Suite := Suite & one;
		else
			Suite := Suite & zero;
			Code_Huffman(Abr.all.Sous_Arbre_Gauche, Suite);
			Code_Huffman(Abr.all.Sous_Arbre_Droit, Suite);
		end if;
	end Code_Huffman;

	procedure ReConsAbr(Abr : in out T_ABR; n : in out Integer; tab_Char : in LCA_Integer_String.T_LCA; Suite : in out Unbounded_String) is
		bit : Character;
		g : T_ABR := null;
		d : T_ABR := null;
	begin

		bit := Element(Suite, 1);
		Suite := Unbounded_Slice(Suite, 2, length(Suite));
		if Abr = null then
			Abr := new T_Noeud;
		end if;
		if bit = '1' then
			Abr.all.Sous_Arbre_Gauche := Null;
			Abr.all.Sous_Arbre_Droit := Null;
			Abr.all.Donnee := LCA_Integer_String.La_Donnee(tab_Char, n);
			n := n + 1;
		else
			g := new T_Noeud;
			Abr.all.Sous_Arbre_Gauche := g;
			ReConsAbr(g, n, tab_Char, Suite);

			d := new T_Noeud;
			Abr.all.Sous_Arbre_Droit := d;
			ReConsAbr(d, n, tab_Char, Suite);
		end if;

	end ReConsAbr;


end ABR_HUFFMAN;
