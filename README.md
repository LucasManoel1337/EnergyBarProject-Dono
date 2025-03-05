Para esse progama ser executado, você deve ter o Maven instalado no computador, caso você não tiver favor instalar.

O banco desse progama está sendo realizado localmente ainda, então caso você queira abrir o progama sem o banco de dados ligado, vai na
classe chamada EnergyBarProject e tem um boolean chamada travaBanco, ao deixar ela true, você irá impedir do progama tentar realizar a 
conexao com o banco de dados. Caso você deixar false, ele irá executar a conexão com o banco de dados, porém caso der um problema, ele
ira abrir uma modal com a mensagem que ocorreu um problema em relação ao banco de dados, possivelmente não exista aquele banco ou algo
do tipo.
