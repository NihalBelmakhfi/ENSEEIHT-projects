
% TP2 de Statistiques : fonctions a completer et rendre sur Moodle
% Nom : BELMAKHFI
% Prénom : Nihal
% Groupe : 1SN-C

function varargout = fonctions_TP2_stat(varargin)

    [varargout{1},varargout{2}] = feval(varargin{1},varargin{2:end});

end

% Fonction centrage_des_donnees (exercice_1.m) ----------------------------
function [x_G, y_G, x_donnees_bruitees_centrees, y_donnees_bruitees_centrees] = ...
                centrage_des_donnees(x_donnees_bruitees,y_donnees_bruitees)
[n,m] = size(x_donnees_bruitees);
x_G = mean(x_donnees_bruitees);
y_G = mean(y_donnees_bruitees);
x_donnees_bruitees_centrees = x_donnees_bruitees - x_G*ones(n,m);
y_donnees_bruitees_centrees = y_donnees_bruitees - y_G*ones(n,m);
   
     
end

% Fonction estimation_Dyx_MV (exercice_1.m) -------------------------------
function [a_Dyx,b_Dyx] = ...
           estimation_Dyx_MV(x_donnees_bruitees,y_donnees_bruitees,n_tests)
    % appel la fonction centrage donnée
[x_G, y_G, x_donnees_bruitees_centrees, y_donnees_bruitees_centrees] = centrage_des_donnees(x_donnees_bruitees,y_donnees_bruitees)
   % Calcul du coeff a_Dyx
psi = rand(n_tests,1)*pi - pi/2;
tg= tan(psi);
   % Traçage de la matrice 
residus = repmat(y_donnees_bruitees_centrees,n_tests,1)-tg * x_donnees_bruitees_centrees;
arg_residus = sum(residus.^2,2);
[val,ind] = min(arg_residus);  
   % On récupère les coefficiens a et b
a_Dyx = tan(psi(ind));
b_Dyx = y_G - a_Dyx*x_G;
end

% Fonction estimation_Dyx_MC (exercice_2.m) -------------------------------
function [a_Dyx,b_Dyx] = ...
                   estimation_Dyx_MC(x_donnees_bruitees,y_donnees_bruitees)
n_tests = length(x_donnees_bruitees);
A = ones(2, n_tests);
A(1,:) = x_donnees_bruitees;
A = A';
B = (y_donnees_bruitees)';
X = A\B;
a_Dyx = X(1);
b_Dyx = X(2);
    
end

% Fonction estimation_Dorth_MV (exercice_3.m) -----------------------------
function [theta_Dorth,rho_Dorth] = ...
         estimation_Dorth_MV(x_donnees_bruitees,y_donnees_bruitees,n_tests)

[x_G, y_G, x_donnees_bruitees_centrees, y_donnees_bruitees_centrees] = centrage_des_donnees(x_donnees_bruitees,y_donnees_bruitees)
Theta = rand(n_tests,1)*pi;
residus = x_donnees_bruitees_centrees.*cos(Theta) + y_donnees_bruitees_centrees.*sin(Theta);
arg_residus = sum(residus.^2,2);
[val,ind] = min(arg_residus); 
theta_Dorth = Theta(ind);
rho_Dorth = x_G*cos(theta_Dorth)+y_G*sin(theta_Dorth);


end

% Fonction estimation_Dorth_MC (exercice_4.m) -----------------------------
function [theta_Dorth,rho_Dorth] = ...
                 estimation_Dorth_MC(x_donnees_bruitees,y_donnees_bruitees)
[x_G, y_G, x_donnees_bruitees_centrees, y_donnees_bruitees_centrees] = centrage_des_donnees(x_donnees_bruitees,y_donnees_bruitees);
C = cat(1, x_donnees_bruitees_centrees, y_donnees_bruitees_centrees);
[vect_propre, val_propre] = eig(C' * C);
[vp_min, indice_min] = min(diag(val_propre));
Y_etoile = vect_propre(:,indice_min);
theta_Dorth = atan2(Y_etoile(2), Y_etoile(1));
rho_Dorth = x_G*cos(theta_Dorth) + y_G*sin(theta_Dorth);

end
