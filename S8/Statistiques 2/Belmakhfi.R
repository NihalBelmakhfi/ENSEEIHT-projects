#BE statistique

#Nihal BELMAKHFI - B1

#1/ Chargement des données
data=read.table(file="Data.txt",header=TRUE)
summary(data)

#2/ Régression simple
lm.out1 = lm(O3~O3v,data)
summary(lm.out1)

#/nuage de points et droite de régression
x11()
plot(data$O3,data$O3v,xlab="O3",ylab="O3v",pch='+')
abline(lm.out1,col="red")

#Analyse des performences de ce modèle
# Après traçage du nuage des points et de la droite de régression, on remarque que sur ce modèle on observe que 
# la regression linéaire passe au centre des valeurs moyennes mais que beaucoup de valeurs sont très éloignées de 
# la droite , alors la relation ne parait donc pas linéaire, le modèle n'est pas adapté à ces données 

#3/ Modèles sans interaction
#analyse du comportement de O3 selon les modalités de RR
dataP <- subset(data, RR=="Pluie")
dataS <- subset(data, RR=="Sec")

#comparaison des échantillons

var.test(dataP$O3,dataS$O3) 
t.test(dataP$O3,dataS$O3,var.equal=T)

# les moyennes sont significativement différentes pour les modalités de RR: précipitations observées à 9h le jour J
# (Pluie ou Sec) avec les valeurs de 73.39535 pour la pluie et 100.84058  pour les jours secs

x11()
par(mfrow=c(1,2))
hist(dataP$O3)
hist(dataS$O3)

# l'histogramme pour le comportement de la concentration d'ozone dans les jours où il pleut a presque la meme allure que 
# celui dans les jours secs

#exploitation des 6 prédicteurs
lm.out2=lm(O3~T+N+FF+O3v+DD+RR, data)
summary(lm.out2)

#Analyse de lm.out2
# Pour la variable NN on obtient une p-value de 0.000285 < 0.05, donc on rejette
# l'hypothèse de nullité du paramètre associé au prédicteur sugar au niveau 0.05, on peut donc considérer
# le prédicteur comme significatif pour expliquer le prédictant O3 

# Pour le facteur DD, le modèle prend ici comme valeur de référence la valeur Est, les expressions
# des données associées à DD correspondent alors à l'influence des directions du vent mesurée à 9h du matin le jour J
# venant du nord, ouest, sud par rapport à l'est. Les p-value de DDNord (0.043753 < 0.05) et de  est de DDSud 
# (0.037149 < 0.05), donc la différence du vent venant du nord et du sud par rapport à celui venant de l'est
# a de l'importance pour le modèle, les paramètre de ces prédicteurs doivent rester non nul, or en ce qui concerne 
# DDOuest (0.581106 > 0.05) alors la différence du vent venant du ouest par rapport à celui venant de l'est est non
#significative pour ce modèle

#hypothèses du s du modèle linéaire gaussien:
#- homoscédasticité :
x11()
plot(fitted(lm.out2),residuals(lm.out2),main="hypothèse homoscedasticite",xlab="Valeurs ajustees (Y*)",ylab="Residus")
# pour l'hypothèse d'homoscédasticité on observe ici que les valeurs ne sont pas vraiment regroupées être répartis 
#de manière uniforme et symétrique autour de zéro. Elles sont plutot dispersées. Il faudrait appliquer une modification 
# L'hypothèse d'homoscédasticité n'est donc pas satisfaite: heteroscedasticite

#- normalité :
x11()
qqnorm(residuals(lm.out2))
# On observe un graphique linéaire et les résidus se situent approximativement le long de la ligne diagonale, 
# cela indique une bonne approximation de la normalité, alors on confirme l'hypothèse de normalité.

x11()
hist(residuals(lm.out2))
# On remarque que l'histogramme présente une forme en cloche (en forme de gaussienne) et que 
#les résidus semblent être centrés autour de zéro, cela suggère une bonne adéquation du modèle à la distribution normale,
#cela confirme ainsi l'hypothèse de normalité

#- indépendance :
x11()
acf(residuals(lm.out2))
# On observe ici que très peu de valeur dépassent la ligne des 0.05, on peut donc conclure que les variables sont bien indépendantes 
# les unes des autres, ce qui valide l'hypothèse d'indépendance. 

#- l'hypothèse de linéarité  :
x11()
plot(fitted(lm.out2),data$O3,xlab="valeur prévues",ylab="valeur observées",pch="+",main="Hypothese de linearite")
# on remarque une legere courbure dans le nuage, l'hypothese de linearite n'est pas completement adaptee

#Prédicteurs à conserver
# Sont les prédicteurs dont la p-value est inférieur au niveau alpha = 0.05: T, N, O3v, DDNord, DDSud
#Pour DDOuest p_value = 0.0430 ~ 0.0430,05: il est donc a la limite du rejet de l'hypothèse de nullité testée 


#Exploitation de l'indice BIC
library(MASS)

lm.outBIC=stepAIC(lm.out2)
summary(lm.outBIC)
# Ce modèle propose de garder les sept prédicteurs précédents dont la p-value était inférieure
# à 0.05, ainsi que DDOuest

/ Modèles avec interactions
lm.outint=lm(O3~.*.,data)
summary(lm.outint)

lm.outBICint=stepAIC(lm.outint,k=log(nrow(data)))
summary(lm.outBICint)
#la dimension, du modèle est : 9

#5/visualisation des prévision
x11()
plot(data$O3, xlab="Indice",ylab="O3")
points(fitted(lm.out1),col="red",pch="+",cex=1.2)
points(fitted(lm.outBICint),col="blue",pch="+",cex=1.2)
# On voit que le modele lm.outBICint en bleue ameliore la regression simple en rouge en permettant une plus grande variabilite aux previsions statistiques (modele plus flexible)

#/evaluation des modèles

# BIAIS et RMSE
# fonction calculant le BIAIS et le RMSE
scores=function(obs,prev) {
rmse=sqrt(mean((prev-obs)**2))
biais=mean(prev-obs)
print("Biais  RMSE") 
return(round(c(biais,rmse),3))
}

# création de data de test et d'apprentissage
nappr=ceiling(0.8*nrow(data))
ii=sample(1:nrow(data),nappr)
jj=setdiff(1:nrow(data),ii)
datatest=data[jj,]
datapp=data[ii,]

# transformation des modèles sur les données d'apprentissage
lm.out1=lm(formula(lm.out1),datapp)
lm.outBIC=lm(formula(lm.outBIC),datapp) 
lm.outBICint=lm(formula(lm.outBICint),datapp)

scores(datapp$O3,fitted(lm.out1))
# "Biais  RMSE"
#  0.000 20.81
scores(datatest$O3,predict(lm.out1,datatest))
# "Biais  RMSE"
# 4.804  19.129
scores(datapp$O3,fitted(lm.outBIC))
# "Biais  RMSE"
#  0.000  11.814
scores(datatest$O3,predict(lm.outBIC,datatest))
# "Biais  RMSE"
# 1.190  10.226
scores(datapp$O3,fitted(lm.outBICint))
# "Biais  RMSE"
#  0.000  11.436
scores(datatest$O3,predict(lm.outBICint,datatest))
# "Biais  RMSE"
# 0.823  9.220

# On observe que pour tous les scores sur les fichiers d'apprentissage les modèles sont non biaisées,
# En ce qui concerne les données de test, ils sont tous biaisés. Le moins biaisé est le modèle BICint.
# Pour le RMSE, il est moins élevé pour les données de test que pour les données d'apprentissage 

#Oui ils sont plus performants que la stratégie triviale, car ils sont mùoins baisés dans les tests

#CV.R

RMSE=function(obs,pr){
return(sqrt(mean((pr-obs)^2)))}

k=100 # nb iterations

tab=matrix(nrow=k,ncol=8)

for (i in 1:k) {

nappr=ceiling(0.8*nrow(data))
ii=sample(1:nrow(data),nappr)
jj=setdiff(1:nrow(data),ii)
datatest=data[jj,]
datapp=data[ii,]

# Estimation des modeles
lm.outAIC=lm(formula(lm.out1),datapp)
lm.outBICint=lm(formula(lm.outBIC),datapp)
lm.outINT=lm(formula(lm.outBICint),datapp)

# Scores sur apprentissage
tab[i,1]=RMSE(datapp$O3,predict(lm.out1))
tab[i,3]=RMSE(datapp$O3,predict(lm.outBIC))
tab[i,4]=RMSE(datapp$O3,predict(lm.outBICint))

# Scores sur test
tab[i,5]=RMSE(datatest$O3,predict(lm.out1,datatest))
tab[i,7]=RMSE(datatest$O3,predict(lm.outBIC,datatest))
tab[i,8]=RMSE(datatest$O3,predict(lm.outBICint,datatest))

}

x11()
boxplot(tab,col=c(rep("blue",4),rep("red",4)),xlab="bleu=apprentissage - rouge=test",
names=c("lm.out1","lm.outBIC","lm.outBICint","lm.out1","lm.outBIC","lm.outBICint"),main=("Modele lineaire gaussien - Score RMSE"))

# Le modèle BICint possède une disperssion autour de la médianes qui est moins importante que celle des autres modèles 
# BIC, il est aussi un des deux modèles les plus bas, et la dispersions en dehors des premiers quartiles est moins élevée avec BIC. 
# On observe pourtant que pour BIC les résultats sur des données de tests sont plus éloignés des données d'apprentissage que BICint. 
# Ce serait donc le modèle BICint le plus adapté à utiliser pour ces données. 


