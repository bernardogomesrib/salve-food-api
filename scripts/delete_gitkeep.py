import os

# Caminho do volume onde os arquivos .gitkeep estão localizados (mapeado no volume do Docker)
volume_path = '/postgres-data'

# Função para percorrer o diretório e remover arquivos .gitkeep
def remove_gitkeep_files(path):
    for root, dirs, files in os.walk(path):
        for file in files:
            if file == '.gitkeep':
                file_path = os.path.join(root, file)
                print(f"Removendo arquivo: {file_path}")
                os.remove(file_path)

if __name__ == "__main__":
    remove_gitkeep_files(volume_path)
