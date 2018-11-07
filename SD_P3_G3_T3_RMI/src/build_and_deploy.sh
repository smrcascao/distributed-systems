javac States/*.java ServerInterface/*.java registry/*.java GeralSide/*.java LojaSide/*.java OficinaSide/*.java ArmazemPMSide/*.java DonaSide/*.java ArtesaosSide/*.java ClientesSide/*.java VectorTime/*.java

cp States/*.class dir_registry/States/
cp VectorTime/*.class dir_registry/VectorTime/
cp ServerInterface/Register.class dir_registry/ServerInterface/
cp registry/*.class dir_registry/registry/

cp States/*.class dir_GeralSide/States/
cp States/*.class dir_LojaSide/States/
cp States/*.class dir_OficinaSide/States/
cp States/*.class dir_ArmazemPMSide/States/

cp VectorTime/*.class dir_GeralSide/VectorTime/
cp VectorTime/*.class dir_LojaSide/VectorTime/
cp VectorTime/*.class dir_OficinaSide/VectorTime/
cp VectorTime/*.class dir_ArmazemPMSide/VectorTime/

cp ServerInterface/*.class dir_GeralSide/ServerInterface/
cp ServerInterface/*.class dir_LojaSide/ServerInterface/
cp ServerInterface/*.class dir_OficinaSide/ServerInterface/
cp ServerInterface/*.class dir_ArmazemPMSide/ServerInterface/

cp GeralSide/*.class dir_GeralSide/GeralSide/
cp LojaSide/*.class dir_LojaSide/LojaSide/
cp OficinaSide/*.class dir_OficinaSide/OficinaSide/
cp ArmazemPMSide/*.class dir_ArmazemPMSide/ArmazemPMSide/



cp VectorTime/*.class dir_DonaSide/VectorTime/
cp VectorTime/*.class dir_ArtesaosSide/VectorTime/
cp VectorTime/*.class dir_ClientesSide/VectorTime/

cp States/*.class dir_DonaSide/States/
cp States/*.class dir_ArtesaosSide/States/
cp States/*.class dir_ClientesSide/States/

cp ServerInterface/*.class dir_DonaSide/ServerInterface/
cp ServerInterface/*.class dir_ArtesaosSide/ServerInterface/
cp ServerInterface/*.class dir_ClientesSide/ServerInterface/

cp DonaSide/*.class dir_DonaSide/DonaSide/
cp ArtesaosSide/*.class dir_ArtesaosSide/ArtesaosSide/
cp ClientesSide/*.class dir_ClientesSide/ClientesSide/

#mkdir /home/sd0303/Public/classes
#mkdir /home/sd0303/Public/classes/ServerInterface

#mkdir /home/sd0303/Public/classes/DonaSide
#mkdir /home/sd0303/Public/classes/ArtesaosSide
#mkdir /home/sd0303/Public/classes/ClientesSide

##cp ServerInterface/*.class /home/sd0303/Public/classes/ServerInterface
#cp set_rmiregistry.sh /home/sd0303
#cp set_rmiregistry_alt.sh /home/sd0303
