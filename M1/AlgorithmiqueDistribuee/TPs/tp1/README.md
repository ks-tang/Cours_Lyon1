# Mif12-AD-TP1

## Étape 3 : Diffusion dans un graphe
### Question 4 + 5
### Topologie en anneau :
| Nombre de nœuds | Nombre de messages |
|-----------------|--------------------|
| 11 (de 0 à 10)  | 20		           |
| 14 (de 0 à 13)  | 26		           |
=>  2 (n-1) messages  
### Tous les nœuds interconnectés :   
| Nombre de nœuds | Nombre de messages |
|-----------------|--------------------|
| 5		          | 16	               |
| 4		          | 9	               |
| 11              | 100	               | 
=> (n-1)^2 messages => (Nombre nœuds - 1) * (Nombre voisin de chaque nœud)  
On ne compte pas le dernier nœud qui n'a pas à transmettre le message puisqu'il "détecte" la terminaison.

## Étape 4 : Construction d'un arbre couvrant
### Tous les nœuds interconnectés : 
| Nombre de nœuds | Nombre de messages construction arbre | Nombre de messages broadcast|
|-----------------|---------------------------------------|-----------------------------|
| 4		          | 18		                              | 3                           |
| 5		          | 32		                              | 4                           |
| 11		      | 200		                              | 10                          |
18 + 3 vs 9 => 2 * 9 + 3  
32 + 4 vs 16 => 2 * 16 + 4  
200 + 10 vs 100 => 2 * 100 + 10  
Dans tous les cas, quand les nœuds sont interconnectés, il suffit de réaliser **trois diffusions**
afin d'amortir le coût de construction.

### Topologie en anneau :
| Nombre de nœuds | Nombre de messages construction arbre | Nombre de messages broadcast|
|-----------------|---------------------------------------|-----------------------------|
| 11		      | 24		                              | 10                          |
| 14		      | 30		                              | 13                          |
34 (24 + 10) vs 20 
43 (30 + 13) vs 26
En moins de **deux diffusions** le coût de construction est amorti.