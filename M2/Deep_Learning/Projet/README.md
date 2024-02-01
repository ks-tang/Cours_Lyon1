# Projet Deep Learning - Classification d'images CIFAR-10 & CIFAR-100

VINCENT Yann p1906701
TANG Kevin p1501263

## Vue d'ensemble
Ce projet vise à classifier des images issues des ensembles de données CIFAR-10 et CIFAR-100 en utilisant le CNN. Chaque dossier contient différents modèles entraînés sur les ensembles de données respectifs.

## Structure du Projet
```
├── modèles
│   ├── cifar10
│   │   ├── AlexNet.h5
│   │   ├── CNN_simple.h5
│   │   ├── DenseNet121.h5
│   │   ├── DenseNet201.h5
│   │   ├── LeNet.h5
│   │   ├── ResNet50.h5
│   │   └── VGGNet.h5
│   └── cifar100
│       ├── AlexNet.h5
│       ├── CNN_simple.h5
│       ├── DenseNet121.h5
│       ├── LeNet.h5
│       └── ResNet50.h5
└── scripts
    ├── cnn_cifar10.ipynb
    └── cnn_cifar100.ipynb
```

## Ensembles de Données
- **CIFAR-10** : Contient 60 000 images couleur de 32x32 pixels réparties en 10 classes différentes.
- **CIFAR-100** : Contient 60 000 images couleur de 32x32 pixels réparties en 100 classes différentes.

## Modèles
Une variété de modèles ont été entraînés sur les ensembles de données CIFAR-10 et CIFAR-100 :
- **AlexNet** : L'une des architectures pionnières du deep learning pour la classification d'images.
- **CNN Simple** : Un modèle de réseau de neurones convolutif de base pour des fins de comparaison.
- **DenseNet121 & DenseNet201** : Des modèles denses en fonctionnalités qui connectent chaque couche à toutes les autres de manière feed-forward.
- **LeNet** : Une architecture CNN classique qui est plus petite et plus rapide à entraîner.
- **ResNet50** : Implémente l'apprentissage résiduel pour faciliter la formation d'architectures de réseau plus profondes.
- **VGGNet** : Connu pour sa simplicité et sa profondeur.

## Scripts
- `cnn_cifar10.ipynb` : Un notebook Jupyter contenant le code pour l'entraînement et l'évaluation des modèles sur CIFAR-10.
- `cnn_cifar100.ipynb` : Un notebook Jupyter contenant le code pour l'entraînement et l'évaluation des modèles sur CIFAR-100.

## Prérequis
Ce projet nécessite Python 3 et les bibliothèques Python suivantes installées :
- NumPy
- Matplotlib
- TensorFlow

## Utilisation
Pour utiliser les modèles entraînés, chargez-les en utilisant l'API Keras de TensorFlow :
```python
from tensorflow.keras.models import load_model

# Exemple pour le modèle AlexNet CIFAR-10
model = load_model('models/cifar10/AlexNet.h5')
```

Pour ré-entraîner ou évaluer les modèles, exécutez les notebooks Jupyter :
- Pour CIFAR-10 : `scripts/cnn_cifar10.ipynb`
- Pour CIFAR-100 : `scripts/cnn_cifar100.ipynb`

## Résultats
Les résultats de la classification des modèles peuvent être visualisés dans les notebooks, y compris les métriques de précision et de perte.

