with Ada.Text_IO;           use Ada.Text_IO;
with Ada.Integer_Text_IO;   use Ada.Integer_Text_IO;
with SDA_Exceptions; 		use SDA_Exceptions;

	--! Les Unbounded_String ont une capacité variable, contrairement au String
	--! pour lesquelles une capacité doit être fixée.test_La_Cell_Next
with LCA;

procedure Test_module_lca is
	procedure Afficher (Cle : in Integer; Donnee: in Character) is
	begin
		Put(Cle,1);
		Put_Line(" --> *"&Donnee);
	end Afficher;

	-- Afficher une  package ABR_String_Integer is
	package LCA_String_Integer is
		new LCA (T_Cle => Integer, T_Donnee => character);
	use LCA_String_Integer;

	procedure Afficher_LCA is
		new LCA_String_Integer.Pour_Chaque(Afficher);

	procedure Enregistrer_Trie is
		new LCA_String_Integer.Enregistrer_Trie("<");


 --on teste la fonction Est_Vide
    procedure test_Est_Vide is
        Sda : T_LCA;
    begin
        initialiser(Sda);
        if Est_Vide(Sda) then
            Put_Line("ok");
            New_Line;
        end if;
		Vider(Sda);
    end test_Est_Vide;

---------------------------------------------------------------------
   procedure test_Taille is
            Sda : T_LCA;

    begin
           initialiser(Sda);
           Enregistrer(Sda,1,'a');
           Enregistrer(Sda,2,'b');
           Enregistrer(Sda,3,'c');
           pragma assert(Taille(sda)=3);
           if Taille(Sda)=3 then
            Put_Line("ok");
            New_Line;
           end if;
		   Vider(Sda);
    end test_Taille;

    -------------------------------------------------------------------
    procedure test_supprimer is
        Sda : T_LCA;
    begin
		initialiser(Sda);
		Enregistrer(Sda,1,'a');
		Enregistrer(Sda,2,'b');
		Enregistrer(Sda,3,'c');
		Supprimer(Sda,3);
		pragma Assert(not Cle_Presente(Sda,3));
		if not Cle_Presente(Sda,3) then
            Put_Line("ok");
            New_Line;
         end if;
		Vider(Sda);
    end test_supprimer;
not Cle_Presente(Sda,3)
    ------------------------------------------------------------------
    procedure test_Cle_Presente is
        sda : T_LCA;
    begin
        initialiser(Sda);
        Enregistrer(Sda,1,'a');
        Enregistrer(Sda,2,'b');
        Enregistrer(Sda,3,'c');
        pragma Assert (Cle_Presente(Sda, 2));
        pragma Assert (not Cle_Presente(Sda, 4));
         if Cle_Presente(Sda, 2) and not Cle_Presente(Sda, 4) then
            Put_Line("ok");
            New_Line;
         end if;
		 Vider(Sda);
    end test_Cle_Presente;

    ------------------------------------------------------------------
    procedure test_La_Paire is
        sda : T_LCA;
    begin
        initialiser(Sda);
        Enregistrer(Sda,1,'a');
        pragma Assert(La_Paire(Sda).Cle =1 and La_Paire(Sda).Donnee ='a');
        if La_Paire(Sda).Cle =1 and La_Paire(Sda).Donnee ='a' then
            Put_Line("ok");
            New_Line;
        end if;
		Vider(Sda);
    end test_La_Paire;

    ----------------------------------------------------------------
   procedure test_La_Cell_Next is
       sda : T_LCA;
       Pair : T_CD;
   begin
       initialiser(Sda);
       Enregistrer(Sda,1,'a');
       Enregistrer(Sda,2,'b');
       Pair := La_Paire(La_Cell_Next(Sda));
       pragma Assert(Pair.Cle = 2 and Pair.Donnee = 'b');
       if Pair.Cle = 2 and Pair.Donnee = 'b' then
            Put_Line("ok");
            New_Line;
      end if;
	  Vider(Sda);
   end test_La_Cell_Next;

    -------------------------------------------------------------------
    procedure test_Enregister_Trie is
        Sda : T_LCA;
		Sda_Trie : T_LCA;
    begin
		initialiser(Sda);
		Enregistrer(Sda,1,'a');
		Enregistrer(Sda,2,'b');
		Enregistrer(Sda,3,'c');
		initialiser(Sda_Trie);
		Enregistrer_Trie(Sda_Trie,3,'c');
		Enregistrer_Trie(Sda_Trie,1,'a');
		Enregistrer_Trie(Sda_Trie,2,'b');
		pragma Assert(La_Paire(Sda_Trie).Cle = 1 and La_Paire(Sda_Trie).Donnee = 'a');
		if La_Paire(Sda_Trie).Cle = 1 and La_Paire(Sda_Trie).Donnee = 'a' then
            Put_Line("ok");
            New_Line;
		end if;
		Afficher_LCA(Sda);
		Afficher_LCA(Sda_Trie);
		Vider(Sda);
		Vider(Sda_Trie);
    end test_Enregister_Trie;

	-------------------------------------------------------------------
    procedure test_Extraire is
        Sda : T_LCA;
		CD : T_CD;
    begin
		initialiser(Sda);
		Enregistrer_Trie(Sda,3,'c');
		Enregistrer_Trie(Sda,2,'b');
		Enregistrer_Trie(Sda,1,'a');
		CD := Extraire_Min(Sda);
		pragma Assert(CD.Cle = 1 and CD.Donnee = 'a');
		if CD.Cle = 1 and CD.Donnee = 'a' then
            Put_Line("ok");
            New_Line;
		end if;
		Vider(Sda);
    end test_Extraire;

begin
    test_Est_Vide;
    test_Taille;
    test_supprimer;
    test_Cle_Presente;
    test_La_Paire ;
    test_La_Cell_Next;
	test_Enregister_Trie;
	test_Extraire;
end Test_module_lca;
