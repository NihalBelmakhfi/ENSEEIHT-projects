With ABR_HUFFMAN ;
With SDA_Exceptions;
With Ada.Text_IO;           use Ada.Text_IO;
with Ada.Strings;           use Ada.Strings;
with Ada.Command_Line;      use Ada.Command_Line;
with Ada.Streams.Stream_IO; use Ada.Streams.Stream_IO;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;

procedure decompresser  is

	-- Afficher l'usage.
	procedure Afficher_Usage is
	begin
		New_Line;
		Put_Line ("Usage : " & Command_Name & " NOM_DU_FICHIER");
		New_Line;
	end Afficher_Usage;
	
	procedure decompression(File_Name : String) is
	
	type T_Octet is mod 2 ** 8;	-- sur 8 bits
	
	package ABR_Integer_String is
		new ABR_HUFFMAN(T_Cle => Integer, T_Donnee => Character, "<" => "<" );
	use ABR_Integer_String;

	use ABR_Integer_String.LCA_Integer_String;
	
	
	id             : Integer;
	char           : Character;
	bit2           : Character;
	S              : Stream_Access;
	bit            : Unbounded_String;
	Octet , Octet2 : T_Octet;
	File           : Ada.Streams.Stream_IO.File_Type;
	suiteBit       : Unbounded_String := Null_Unbounded_String;
	
	abr , abr_tmp  : T_ABR;
	lca            : ABR_Integer_String.LCA_Integer_String.T_LCA;

	

	begin

		Open(File, In_File, File_Name);
		S := Stream(File);

		-- Retrouver les caractères du texte à décompresser et les associer à leur indice
		-- d'ordre d'apparition dans l'arbre d'huffman
		id := 0;
		Octet := T_Octet'Input(S);
		loop
			Octet2 := T_Octet'Input(S);
			Char := Character'Val(Octet);
			Enregistrer(lca, id, Char);
			id := id + 1;
			exit when Octet = Octet2;
			Octet := Octet2;
		end loop;

		-- Enregistrer le reste des octets du fichier compressé dans une chaine de
		-- caractère pour faciliter la manipulation des données
		while not End_Of_File(File) loop

			Octet := T_Octet'Input(S);
			for i in 1..8 loop
				Octet2 := Octet / 128;
				bit := trim(To_Unbounded_String(Integer'Image(Integer(Octet2))), Both);
				suiteBit := suiteBit & bit;
				Octet := Octet * 2;
			end loop;

		end loop;
	
		Close(File);
	

		-- Reconstruire l'arbre d'huffman  à partir du codage d'huffman de l'arbre
		-- récupérer dans le fichier compressé
		Initialiser(abr);
		id := 0;
		ReConsAbr(abr, id, lca, suiteBit);
	

	
		Create (File, Out_File, File_Name(1..(File_Name'Last - 4)) & "_decompresse.out");
		S := Stream (File);
		
		--lire bit à bit le reste du fichier et lorsque le codage d'huffman d'un caractère
		-- est reconnu on ecrit ce dernier dans le fichier décompressé
		abr_tmp := abr;
		Char := La_Donnee(abr_tmp);
		while Char /= '$' loop
			if Est_Feuille(abr_tmp) then
				T_Octet'Write(S, T_Octet(Character'Pos(Char)));
				abr_tmp := abr;
			else
				bit2 := Element(suiteBit, 1);
				suiteBit := Unbounded_Slice(suiteBit, 2, length(suiteBit));
				if bit2 = '0' then
					abr_tmp := SAG(abr_tmp);
				else
					abr_tmp := SAD(abr_tmp);
				end if;
			end if;
			Char := La_Donnee(abr_tmp);
		end loop;

		Close (File);
		
		Vider(lca);
		Vider(abr);
	

	
	end decompression;
	
begin

	if Argument_Count /= 1 then
		Afficher_Usage;
	else
		decompression(Argument(1));
	end if;
	
exception
	when Constraint_Error =>
		Afficher_Usage;
end decompresser;
