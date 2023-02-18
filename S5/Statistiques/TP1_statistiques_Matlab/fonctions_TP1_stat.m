
% TP1 de Statistiques : fonctions a completer et rendre sur Moodle
% Nom : BELMAKHFI
% Prénom : Nihal
% Groupe : 1SN-C

function varargout = fonctions_TP1_stat(varargin)
    [varargout{1},varargout{2}] = feval(varargin{1},varargin{2:end});
end

% Fonction G_et_R_moyen (exercice_1.m) ----------------------------------
function [G, R_moyen, distances] = ...
         G_et_R_moyen(x_donnees_bruitees,y_donnees_bruitees)
[n,m] = size(x_donnees_bruitees);
Gx = mean(x_donnees_bruitees);
Gy = mean(y_donnees_bruitees);
G =[Gx Gy];
distances = sqrt((x_donnees_bruitees-Gx*ones(n,m)).*(x_donnees_bruitees-Gx*ones(n,m)) + (y_donnees_bruitees-Gy*ones(n,m)).*(y_donnees_bruitees-Gy*ones(n,m)));
R_moyen = mean(distances);
    
end

% Fonction estimation_C_uniforme (exercice_1.m) ---------------------------
function [C_estime, R_moyen] = ...
         estimation_C_uniforme(x_donnees_bruitees,y_donnees_bruitees,n_tests)
     
[G, R_moyen, distances] = G_et_R_moyen(x_donnees_bruitees,y_donnees_bruitees)    
N = length(x_donnees_bruitees);
C_test = G + [2*rand(n_tests, 2)-1]*R_moyen;
ecart_x = repmat(x_donnees_bruitees, n_tests, 1)-repmat(C_test(:,1),1,N);
ecart_y = repmat(y_donnees_bruitees, n_tests, 1)-repmat(C_test(:,2),1,N);
residus = sqrt(ecart_x.^2 + ecart_y.^2) - R_moyen;
argument_residus = sum(residus.^2, 2);
[Min, pos] = min(argument_residus);
C_estime = C_test(pos,:)
end

% Fonction estimation_C_et_R_uniforme (exercice_2.m) ----------------------
function [C_estime, R_estime] = ...
         estimation_C_et_R_uniforme(x_donnees_bruitees,y_donnees_bruitees,n_tests)

[G, R_moyen, distances] = G_et_R_moyen(x_donnees_bruitees,y_donnees_bruitees);  
N = length(x_donnees_bruitees);
C_test = G + [2*rand(n_tests, 2)-1]*R_moyen;
R_test = R_moyen + [2*rand(n_tests, 1)-1]*R_moyen;
ecart_x = repmat(x_donnees_bruitees, n_tests, 1)-repmat(C_test(:,1),1,N);
ecart_y = repmat(y_donnees_bruitees, n_tests, 1)-repmat(C_test(:,2),1,N);
residus = sqrt(ecart_x.^2 + ecart_y.^2) - repmat(R_test,1,N);
argument_residus = sum(residus.^2, 2);
[Min, pos] = min(argument_residus);
C_estime = C_test(pos,:);
R_estime = R_test(pos,:);
end

% Fonction occultation_donnees (donnees_occultees.m) ----------------------
function [x_donnees_bruitees, y_donnees_bruitees] = ...
         occultation_donnees(x_donnees_bruitees, y_donnees_bruitees, theta_donnees_bruitees)

theta1 = rand(1,1)*2*pi;
theta2 = rand(1,1)*2*pi;
if theta1 < theta2
    indices = find(theta_donnees_bruitees>= theta1 & theta_donnees_bruitees<=theta2);
elseif theta1 > theta2
    indices = find(theta_donnees_bruitees>= theta1 | theta_donnees_bruitees<=theta2);
end

if isempty(indices)
    indices = 1;
end
    x_donnees_bruitees = x_donnees_bruitees(indices);
    y_donnees_bruitees = y_donnees_bruitees(indices);
end
% Fonction estimation_C_et_R_normale (exercice_4.m, bonus) ----------------
function [C_estime, R_estime] = ...
         estimation_C_et_R_normale(x_donnees_bruitees,y_donnees_bruitees,n_tests)



end
