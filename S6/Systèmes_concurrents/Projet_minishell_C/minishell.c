#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdbool.h>
#include <sys/stat.h>
#include "readcmd.h"
#include "readcmd.c"
#include <stdio.h>
#include <sys/wait.h>
#include <string.h>
#include <signal.h>
#include <errno.h>
#include <fcntl.h>
#include <assert.h>



                             
typedef enum{
    background, foreground, suspendu
    } state;

typedef struct job{
    int identifiant; //il nous faut un processus unique pour chaque processus
    pid_t pid;
    state etat;
    char* lig_commande;
    } job;

typedef struct tab_job{
    job* tab; 
    int taille; //nbr de processus en exécussion
    int quel_minishell;}tab_job;

//Variable permettant l'utilisation de susp
  bool IsSusp = false;
  
//Variable contenant le pid du processus en avant plan à chaque instant
    pid_t pidFils_foreground;

void add_job(pid_t pid_job, state etat, char* commande, tab_job *tab_affichage){
    //++ tab_affichage->taille;
    //++ tab_affichage->quel_minishell;
    tab_affichage->tab = realloc(tab_affichage->tab, (tab_affichage->taille+1)*sizeof(job));        
    tab_affichage->tab[tab_affichage->taille].identifiant = tab_affichage->quel_minishell+1;
    tab_affichage->tab[tab_affichage->taille].pid = pid_job; //on ajoute tous les éléments 
    tab_affichage->tab[tab_affichage->taille].etat = etat;   
    tab_affichage->tab[tab_affichage->taille].lig_commande = malloc(20*sizeof(char));
    strcpy(tab_affichage->tab[tab_affichage->taille].lig_commande, commande);
    ++ tab_affichage->taille;
    ++ tab_affichage->quel_minishell;
}    
    
    
void show_job(job my_job, tab_job *tab_affichage){
    assert(tab_affichage != NULL);       
    if(tab_affichage->taille ==0){
        printf("aucun job\n");}
    else{
        for(int i=0; i< tab_affichage->taille; i++){
            if (tab_affichage->tab[i].etat == background){
                printf("identifiant: %d      pid: %d      actif(background)      ligne de commande: %s\n", tab_affichage->tab[i].identifiant, tab_affichage->tab[i].pid, tab_affichage->tab[i].lig_commande);
                fflush(stdout);}
        
    if (tab_affichage->tab[i].etat == foreground){
        printf("identifiant: %d      pid: %d      actif(foreground)      ligne de commande: %s\n", tab_affichage->tab[i].identifiant, tab_affichage->tab[i].pid, tab_affichage->tab[i].lig_commande);
        fflush(stdout);}
        
    if (tab_affichage->tab[i].etat == suspendu){
        printf("identifiant: %d      pid: %d      suspendu      ligne de commande: %s\n", tab_affichage->tab[i].identifiant, tab_affichage->tab[i].pid, tab_affichage->tab[i].lig_commande);
        fflush(stdout);}
        }       
        }}
        
        
void delete_job( pid_t pid_job, tab_job *tab_affichage){
    assert(tab_affichage != NULL);
    int job;
    int i;
    for(i=0; i< tab_affichage->taille; ++i){
        if(tab_affichage->tab[i].pid == pid_job){
            job = i;
        }
    }
     tab_affichage->taille -= 1; 
     for(int j=job; j< tab_affichage->taille; ++j){
        tab_affichage->tab[j] = tab_affichage->tab[j+1];
     } 
     tab_affichage->tab = realloc(tab_affichage->tab, sizeof(job)*(tab_affichage->taille));          
}   

int ind_job(int id_job, tab_job *tab_affichage) {
    assert(tab_affichage != NULL);
    int My_job;
    for(int i=0; i< tab_affichage->taille; i++) {
        if (tab_affichage->tab[i].identifiant == id_job) {
            My_job = i;
        }
    }
    return My_job;
}


void Susp_job (int identifiant_job, tab_job *tab_affichage){
    assert(tab_affichage != NULL);
    bool Exist_job = false;
    for(int i=0; i<tab_affichage->taille; i++){
        if(identifiant_job == tab_affichage->tab[i].identifiant){
		 kill(tab_affichage->tab[i].pid, SIGSTOP); /*Envoi du signal SIGSTOP, pour suspendre le job */           			
		 tab_affichage->tab[i].etat = suspendu;            
         Exist_job = true;
      } 
    }
    if (!Exist_job){
      printf("Cet identifiant ne correspond à aucun job\n");
    }
  }

pid_t currentPid;

void foregrounded_job (int identifiant_job, tab_job *tab_affichage){
    assert(tab_affichage != NULL);
    bool Exist_job = false;
    for(int i=0; i<tab_affichage->taille; i++){
        if(identifiant_job == tab_affichage->tab[i].identifiant){
		 pidFils_foreground = tab_affichage->tab[i].pid;
		 currentPid = pidFils_foreground;
            kill(tab_affichage->tab[i].pid, SIGCONT);             		
            tab_affichage->tab[i].etat = foreground;
            sleep(1);
		 //waitpid(pidFils_foreground, &status, WUNTRACED);
		 pause();
            Exist_job = true;
      } 
    }
    if (!Exist_job){
      printf("Cet identifiant ne correspond à aucun job\n");
    }
  }

  void backgrounded_job (int identifiant_job, tab_job *tab_affichage){
    assert(tab_affichage != NULL);
    bool Exist_job = false;
    for(int i=0; i<tab_affichage->taille; i++){
        if(identifiant_job == tab_affichage->tab[i].identifiant){           
 		 kill(tab_affichage->tab[i].pid, SIGCONT); 
            tab_affichage->tab[i].etat = background;
            Exist_job = true;
      } 
    }
    if (!Exist_job){
      printf("Cet identifiant ne correspond à aucun job\n");
    }
  }
  
//on traite les différentds signaux       
tab_job *tab_affichage;


    void handlerSIGCHLD(int signal){
    int pid_fils;
    int etat_curr;
    while ((pid_fils = (int) waitpid(-1, &etat_curr, WUNTRACED | WCONTINUED | WNOHANG)) > 0) {
        if ((pid_fils == -1) || (pid_fils == 0)){
            perror("waitpid");
            break;
        } else if(WIFCONTINUED(etat_curr)) {
        //
        }
        else if (WIFEXITED(etat_curr) || WIFSIGNALED(etat_curr)) {
            delete_job(pid_fils, tab_affichage);}
        else if (WIFSTOPPED(etat_curr)) {
            for(int i=0; i<tab_affichage->taille; i++){
                if(pid_fils == tab_affichage->tab[i].pid){      
                    Susp_job (tab_affichage->tab[i].identifiant, tab_affichage);
                }
            }
        }
            //kill(pid_fils, SIGKILL);
     }
    return;
}


void handlerSIGTSTP(int signal) {
    kill(currentPid, SIGSTOP);
  }
  

void handlerSIGINT(int signal) {
    //int r = 
    kill(currentPid, SIGKILL);
}


                                 
void commandes_internes(tab_job *tab_affichage, char* commande, char* argv[], int *boucler, job my_job, bool *Is_cmdInterne, char* argv_de_saisie) {
    *boucler = 1;
    *Is_cmdInterne = false;
    if (!strcmp(commande, "cd")){
        if (*(argv + 1) != NULL){
            chdir(argv[1]);
            }
        else{
            const char* HOME = getenv("HOME");
            chdir(HOME); //getenv retourne un pointeur
            }
            *Is_cmdInterne = true;
    } 
    if (!strcmp(commande, "exit")){ //strcm=0 si les deux chaines de caractères sont égaux
        printf("\nSalut\n");
        *boucler = 0;
        *Is_cmdInterne = true;
    }
    else if (!strcmp(commande, "lj")) {          
	    show_job(my_job, tab_affichage); 
	    *Is_cmdInterne = true;	           
    }
    else if (!strcmp(commande, "sj")) {		
        int identifiant_suspension = atoi(argv_de_saisie);
        Susp_job(identifiant_suspension, tab_affichage);
        *Is_cmdInterne = true;
    }
    else if (!strcmp(commande, "fg")) {
        if(!argv){
            printf("y a pas d'arg \n");}
        else{
		    int identifiant_foreground = atoi(argv_de_saisie);
            foregrounded_job(identifiant_foreground, tab_affichage);
            *Is_cmdInterne = true;}
    }
    else if(!strcmp(commande, "bg")) {
	  int identifiant_background = atoi(argv_de_saisie);
        backgrounded_job(identifiant_background, tab_affichage);
        *Is_cmdInterne = true;
    }
}

//pid_t pidFils;        
//tab_job *tab_affichage;
//int status;
//void traitant(int sigcode){
    //waitpid(pidFils, &status, 0);
    //pidFils = (int) waitpid(-1, &etat, WUNTRACED | WCONTINUED);
    //delete_job(pidFils, tab_affichage); 
    //kill(pidFils, SIGSTOP);
//} 

                
                                                                                                                               
int main(int argc, char* argv[]){//, tab_job tab_affichage, job my_job){
    tab_affichage = malloc(sizeof(tab_job));
    job *my_job = malloc(sizeof(job));
    int codeTerm;
    pid_t pidFils;  //, idFils;
    struct cmdline *s;
    char ** arg;

    int boucler; 
    boucler = 1;

    tab_affichage->taille = 0;
    //bool Is_cmdInterne = false;

	
    do {
	  bool Is_cmdInterne = false;  
        printf(">>>write something: ");
        do {
        s = readcmd();
        } while (s == NULL);
            if (s->seq[0] != NULL) {
            arg = (s->seq)[0] ;
            if (s->err == NULL) {       
            //ne rien faire au cas d'un simple retour à la ligne.          
        
        commandes_internes(tab_affichage, arg[0], arg, &boucler, *my_job, &Is_cmdInterne, arg[1]);
        //if (! Is_cmdInterne){
        if (!Is_cmdInterne){
            pidFils = fork(); //on crée un fils    

 
		//les signaux
	    struct sigaction CHLDsa;
	    CHLDsa.sa_handler = handlerSIGCHLD;
	    sigaction(SIGCHLD, &CHLDsa, NULL);
	    
	    struct sigaction TSTPsa;
	    TSTPsa.sa_handler = handlerSIGTSTP;
	    sigaction(SIGTSTP, &TSTPsa, NULL);
	    
	    struct sigaction INTsa;
	    INTsa.sa_handler = handlerSIGINT;
	    sigaction(SIGINT, &INTsa, NULL);

	    
	    
    //signal(SIGCHLD, handlerSIGCHLD);
    //signal(SIGTSTP, handlerSIGTSTP);
    //signal(SIGINT, handlerSIGINT);
    //signal(SIGUSR1, handlerSIGINT);

            if (pidFils == -1){
                printf("erreur dans fork\n");
                exit(1); //si le fils ne se construit pas correctement         
            }     
                   
            else if (pidFils == 0) { //fils
            	if (s->in != NULL) {
                        /*redirection de l'entrée standard*/
                        int desc_fich_in, dup_desc;
						desc_fich_in = open (s->in, O_RDONLY);
						if (desc_fich_in < 0) {
							perror(s->in);
							exit(1);
						}
						dup_desc = dup2(desc_fich_in, 0);
						if (dup_desc == -1) {
							printf("Erreur dup2 \n");
							exit(1);
    }
                	}
                if (s->out != NULL) {
                	/*redirection de la sortie standard*/
                	int desc_fich_out, dup_desc;
					desc_fich_out = open (s->out, O_WRONLY| O_CREAT | O_TRUNC, 0640);

					if (desc_fich_out < 0) {
						perror(s->out);
						exit(1);
					}

					dup_desc = dup2(desc_fich_out, 1);

					if (dup_desc == -1) {
						printf("Erreur dup2 \n");
						exit(1);
					}
									}
                if (boucler) {
                     execvp(arg[0], arg); // la commande et un tableau d'args
                     exit(2); // qd on a erreur, exit(2)  s'exécute
                }
                else{ 
                    exit(4);
                }
               exit(EXIT_SUCCESS); // cette ligne s'exécute lorsque execlp s'exécute
                
             }             
             else{ //père
                char* bag = s->backgrounded;
                if(bag == NULL){
                add_job(pidFils, foreground, arg[0], tab_affichage);
 			    pidFils_foreground = pidFils;
 			    currentPid = pidFils_foreground;          
			    //waitpid(pidFils, &status, WUNTRACED);
			    pause();
			    //delete_job(pidFils, tab_affichage);            
                    //idFils = waitpid(pidFils, &status, 0);} //pour que le père attends le fils avant de mourir
                    }        
    
    
                 else{
                    add_job(pidFils, background, arg[0], tab_affichage);      
                    }
                    //wait(&status);  
			        //delete_job(pidFils, tab_affichage);  
                    
             //if(idFils == -1){
                //perror("wait");
                //exit(2);}
                
             if(WEXITSTATUS(codeTerm) == 2) { //récupère les exits
                        printf("ECHEC\n");
                       }
                    else if (WEXITSTATUS(codeTerm) == 4){
                        printf("\nSalut\n");
                        exit(EXIT_SUCCESS);}
                    else {    
                        printf("SUCCES\n");}          
             }   
                             
         }
         else{
            //nombre de tubes
                int N_tubes = 1;
                while( s->seq[N_tubes] != NULL) {
                    N_tubes++;
                }
                N_tubes--;
                int pipes[2*N_tubes];
                int wstatus, pid;
                for (int i = 0; i< N_tubes; i++) {
                    if (pipe(pipes + i*2) < 0) {
                        perror("on a erreur dans pipe");
                        exit(1);
                    }
                }
                int j = 0;
                int k = 0;// l'indice de la commande
                while (s->seq[k] != NULL) {
                    pid = fork();
                    if (pid < 0) {
                        perror("on a erreur dans fork");
                        exit(1);
                    }
                    else if (pid == 0) {
                        //les redirections < et > pour la première et dernière commande
                        if (k == 0) {
                            if (s->in != NULL) {
                                //redirection ;
                                int descFich_in, dup_desc;
                                descFich_in = open (s->in, O_RDONLY);
                                if (descFich_in < 0) {
                                    perror(s->in);
                                    exit(1);
                                }
                                dup_desc = dup2(descFich_in, 0);
                                if (dup_desc == -1) {
                                    printf("on a erreur dans dup2 \n");
                                    exit(1);
                                }
                            }
                        }
                        if ( k == N_tubes ) {
                            if (s->out != NULL) {
                                //redirection 
                                int descFich_out, dup_desc;
                                descFich_out = open (s->out, O_WRONLY| O_CREAT | O_TRUNC, 0640);

                                if (descFich_out < 0) {
                                    perror(s->out);
                                    exit(1);
                                }

                                dup_desc = dup2(descFich_out, 1);

                                if (dup_desc == -1) {
                                    printf("on a erreur dans dup2 \n");
                                    exit(1);
                                }
                            }
                        }
                        //On redirige l'entrée standard vers la sortie du dernier tube pipes[j-2] si ce n'est pas la première commande
                        if (j != 0) {
                            if (dup2(pipes[j-2], 0) < 0 ) {
                                perror("on a erreur dans dup2");
                                exit(1);
                            }
                        }
                        //si ce n'est pas la derniére commande, 
                        //On redirige la sortie standard vers l'entrée pipes[j+1] si ce n'est pas la dernière commande
                        if (s->seq[k+1] != NULL) {
                            if (dup2(pipes[j+1], 1) < 0) {
                                perror("on a erreur dans dup2");
                                exit(1);
                            }
                        }
                        //fermer les tubes après les redirections
                        for (int i =0; i < 2*N_tubes; i++) {
                            close(pipes[i]);
                        }
                        if (execvp(s->seq[k][0], s->seq[k]) < 0) {
                            perror("on a erreur dans exec");
                            exit(1);
                        }
                    }
                    k++; j+=2;
                }
                //le père ferme tous les tubes et attend ses fils
                for (int i =0; i < 2*N_tubes; i++) {
                    close(pipes[i]);
                }
                for (int i =0; i < N_tubes +1; i++) {
                    wait(&wstatus);
                }
         }
         }
         }
         
         }while (boucler);
             //return EXIT_SUCCESS; /* -> exit(EXIT_SUCCESS); pour le père */  
     }
