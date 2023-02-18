
% TP3 de Statistiques : fonctions a completer et rendre sur Moodle
% Nom : Belmakhfin
% Prenom : Nihal
% Groupe : 1SN-C

function varargout = fonctions_TP3_stat(varargin)

    [varargout{1},varargout{2}] = feval(varargin{1},varargin{2:end});

end

% Fonction estimation_F (exercice_1.m) ------------------------------------
function [rho_F,theta_F,ecart_moyen] = estimation_F(rho,theta)
A = [cos(theta),sin(theta)];
X = inv(A'*A)*A'*rho;
    %defintion de xf et yf
xf = X(1);
yf = X(2);
rho_F = sqrt(xf^2 + yf^2);
theta_F = atan2(yf,xf);





    % A modifier lors de l'utilisation de l'algorithme RANSAC (exercice 2)
    ecart_moyen = Inf;

end

% Fonction RANSAC_2 (exercice_2.m) ----------------------------------------
function [rho_F_estime,theta_F_estime] = RANSAC_2(rho,theta,parametres)
S1 = parametres(1);
S2 = parametres(2);
kmax = parametres(3);


% etape:1 tirage aléatoire d'un rho et theta et estimation des paramètres du modèle


for k = 1: kmax
    % initialisation de l'écart moyen minimal
    ecart_moyen_min = Inf;
    n = length(rho);
    ind = randperm(n,2);  %récupération des indices entre 1 et n données
    [rho_F,theta_F,ecart_moyen] = estimation_F(rho(ind),theta(ind));
    
    
    % vérification de la validité du modèle 
    conforme = abs(rho - rho_F * cos(theta - theta_F)) < S1;
    nbr_conforme = sum(conforme,1);
    proportion = nbr_conforme/n;
    if proportion > S2 
        % on refait l'estimation avec les données conformes
        [rho_F,theta_F,ecart_moyen] = estimation_F(rho(conforme),theta(conforme));
        if ecart_moyen < ecart_moyen_min
            rho_fin = rho_F;
            theta_fin = theta_F;
            ecart_moyen_fin = ecart_moyen;
        end   
    end
end
rho_F_estime = rho_fin;
theta_F_estime = theta_fin;
end


  

% Fonction G_et_R_moyen (exercice_3.m, bonus, fonction du TP1) ------------
function [G, R_moyen, distances] = ...
         G_et_R_moyen(x_donnees_bruitees,y_donnees_bruitees)

    Longueur = length(x_donnees_bruitees);
     G = [sum(x_donnees_bruitees)/Longueur,sum(y_donnees_bruitees)/Longueur];
    Distance = sqrt((x_donnees_bruitees - G(1)).^2 + (y_donnees_bruitees - G(2)).^2);
    R_moyen = sum(Distance)/Longueur;
    distances = Distance;

end

% Fonction estimation_C_et_R (exercice_3.m, bonus, fonction du TP1) -------
function [C_estime,R_estime,critere] = ...
         estimation_C_et_R(x_donnees_bruitees,y_donnees_bruitees,n_tests,C_tests,R_tests)
     
    % Attention : par rapport au TP1, le tirage des C_tests et R_tests est 
    %             considere comme etant deje effectue 
    %             (il doit etre fait au debut de la fonction RANSAC_3)

[G, R_moyen, distances] = ...
         G_et_R_moyen(x_donnees_bruitees,y_donnees_bruitees);

    L= length(x_donnees_bruitees);
    C = C_tests;
    R = R_tests;

    Cdupliquee_x = repmat(C(:,1),1,L);
    Cdupliquee_y = repmat(C(:,2),1,L);

    diff_x = repmat(x_donnees_bruitees,n_tests,1) - Cdupliquee_x;
    diff_y = repmat(y_donnees_bruitees,n_tests,1) - Cdupliquee_y;

    Residu = sqrt(diff_x.^2 + diff_y.^2) - repmat(R,1,L);
    Arg_Residu = sum(Residu.^2,2);

    [~,indice_min] = min(Arg_Residu);

    C_estime = C(indice_min,:);
    R_estime = R(indice_min,:);


end

% Fonction RANSAC_3 (exercice_3, bonus) -----------------------------------
function [C_estime,R_estime] = ...
         RANSAC_3(x_donnees_bruitees,y_donnees_bruitees,parametres)
     
    % Attention : il faut faire les tirages de C_tests et R_tests ici
[G, R_moyen, distances] = ...
         G_et_R_moyen(x_donnees_bruitees,y_donnees_bruitees);
    n_tests = parametres(4);
    C_tests = ((2*rand(n_tests,2)-1)*R_moyen) + G;
    R_tests = R_moyen*(rand(n_tests,1)+0.5);


end
