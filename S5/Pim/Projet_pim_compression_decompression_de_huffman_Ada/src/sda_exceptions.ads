-- Définition d'une exception commune à toutes les SDA.
package SDA_Exceptions is

	Cle_Absente_Exception  : Exception;	-- une clé est absente d'un SDA
	Empty_Tree_Exception   : Exception;  --un abr est vide
	emptyList_Exception    : exception; --une liste chainées(LCA) est vide
end SDA_Exceptions;
