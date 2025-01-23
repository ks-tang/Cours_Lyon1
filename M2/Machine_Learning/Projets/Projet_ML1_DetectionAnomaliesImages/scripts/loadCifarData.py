import os
import pickle
import numpy as np
import pandas as pd


_DIR_ = os.path.dirname(os.path.abspath(__file__))

def unpickle(file):    
    with open(file, 'rb') as fo:
        # Decommanter cette ligne si problème de décodage
        # dict_cifar = pickle.load(fo, encoding='bytes')
        dict_cifar = pickle.load(fo, encoding='latin-1')
    return dict_cifar

def extract_cifar_from_dict(file):
    dict_cifar = unpickle(file)
    df_cifar = pd.DataFrame(dict_cifar['data'])
    df_cifar['target'] = dict_cifar['labels']
    return df_cifar

def create_binary_label(df):
    df['target'] = df['target'].apply(lambda x: 1 if x==0 else 0)
    return df

def export_Df_Csv(df, path, csv_name, index_=False):
    path = os.path.join(_DIR_, path)
    df.to_csv(path + csv_name, index=index_)

def export_DfXY_Csv(X, y, path, csv_name, index_=False):
    path = os.path.join(_DIR_, path)
    df = pd.concat([X, y], axis=1)
    df.to_csv(path + csv_name, index=index_)

def split_train_test_cifar(df_cifar):
    # Séparation des features et des labels
    X = df_cifar.drop('target', axis=1)
    y = df_cifar['target']
    return X, y

def load_one_cifar(file):
    file_path = os.path.join(_DIR_, file)
    df_cifar = extract_cifar_from_dict(file_path)
    df_cifar = create_binary_label(df_cifar)
    X, y = split_train_test_cifar(df_cifar)
    return X, y

def load_train_cifar(withExport=False, path='../data/', name='ref_data.csv', nb_batch=5):
    X_train, y_train = load_one_cifar('../cifar_img/data_batch_1')
    for i in range(2, nb_batch+1):
        X, y = load_one_cifar('../cifar_img/data_batch_' + str(i))
        X_train = pd.concat([X_train, X])
        y_train = pd.concat([y_train, y])

    # Création fichier ref_data.csv
    if withExport:
        export_DfXY_Csv(X_train, y_train, path, name, index_=False)

    return X_train, y_train
    
def load_test_cifar(withExport=False, path='../data/', name='ref_data_Test.csv'):
    X_test, y_test = load_one_cifar('../cifar_img/test_batch')
    # Création fichier ref_data.csv
    if withExport:
        export_DfXY_Csv(X_test, y_test, path, name, index_=False)
    return X_test, y_test


def load_all_cifar(exportTrainCsv=False, exportTestCsv=False):
    X_train, y_train = load_train_cifar(exportTrainCsv)
    X_test, y_test = load_test_cifar(exportTestCsv)
    return X_train, y_train, X_test, y_test

# Script pour convertir les images CIFAR en csv
load_all_cifar(exportTrainCsv=True, exportTestCsv=True)
