package server.protocol_Proto;

import client.Domain_Simplu.*;
import server.dto.ConcursDTO;
import server.dto.InscriereDTO;
import server.dto.OrganizatorDTO;
import server.dto.ParticipantDTO;

import java.util.ArrayList;
import java.util.List;

public class Utils_Proto {
    public static Protobuf.OrganizaotrPB convertOrganizatorDTO_ToProto(OrganizatorDTO organizator) {
        return Protobuf.OrganizaotrPB.newBuilder()
                .setLastName(organizator.getLastName())
                .setFirstName(organizator.getFirstName())
                .setParola(organizator.getParola()).build();
    }

    public static OrganizatorDTO convertOrganizatorPB_ToOrganizatorDTO(Protobuf.OrganizaotrPB organizatorPB) {
        return new OrganizatorDTO(organizatorPB.getLastName(), organizatorPB.getFirstName(), organizatorPB.getParola());
    }
    public static Protobuf.ConcursPB convertConcursDTO_ToProto(ConcursDTO concurs) {
        return Protobuf.ConcursPB.newBuilder()
                .setId(concurs.getId())
                .setProba(concurs.getProga().name())
                .setCategorie(concurs.getCategorie().name()).build();
    }

    public static InscriereDTO convertInscrierePB_toInscriereDTO(Protobuf.InscriereFullPB inscrierePB) {
        return new InscriereDTO(inscrierePB.getId(), convertParticipantDTO_ToParticipant(convertParticipantPB_ToParticipantDTO(inscrierePB.getParticipant())), convertConcursDTO_ToConcurse(convertConcursPB_ToConcursDTO(inscrierePB.getConcurs())));
    }

    public static Protobuf.ParticipantPB convertParticipantDTO_ToProto(ParticipantDTO participant) {
        return Protobuf.ParticipantPB.newBuilder()
                .setId(participant.getId())
                .setLastName(participant.getLastName())
                .setFirstName(participant.getFirstName())
                .setAge(participant.getAge()).build();
    }

    public static Protobuf.ParticipantFaraIdPB convertParticipantDTO_ToProtoFaraId(String nume, String prenume, Integer varsta) {
        return Protobuf.ParticipantFaraIdPB.newBuilder()
                .setLastName(nume)
                .setFirstName(prenume)
                .setAge(varsta).build();
    }


    public static List<String> convertParticipantFaraIdPB_ToList(Protobuf.ParticipantFaraIdPB participantFaraIdPB) {
        List<String> list = new ArrayList<>();
        list.add(participantFaraIdPB.getLastName());
        list.add(participantFaraIdPB.getFirstName());
        list.add(String.valueOf(participantFaraIdPB.getAge()));
        return list;
    }

    public static Protobuf.Response FIND_ALL_PARTICIPANTS(Iterable<ParticipantDTO> participants) {
        List<Protobuf.ParticipantPB> participantPBList = new ArrayList<>();
        for (ParticipantDTO participant : participants) {
            participantPBList.add(convertParticipantDTO_ToProto(participant));
        }

        Protobuf.Response response = Protobuf.Response.newBuilder()
                .setType(Protobuf.Response.ResponseType.OK)
                .addAllParticipantList(participantPBList)
                .build();

        return response;
    }

    public static List<String> convertInscriereFaraId_ToList(Protobuf.InscriereFaraIdPB inscriereFaraIdPB) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(inscriereFaraIdPB.getIdParticipant()));
        list.add(String.valueOf(inscriereFaraIdPB.getIdConcurs()));
        return list;
    }

    public static Protobuf.InscriereFullPB convertInscriereDTO_ToInscriereFullPB(InscriereDTO inscriere) {
        Protobuf.ParticipantPB participantPB = convertParticipantDTO_ToProto(convertParticipant_ToParticipantDTO(inscriere.getParticipant()));
        Protobuf.ConcursPB concursPB = convertConcursDTO_ToProto(convertConcurs_ToConcursDTO(inscriere.getConcurs()));

        return Protobuf.InscriereFullPB.newBuilder()
                .setId(inscriere.getId())
                .setParticipant(participantPB)
                .setConcurs(concursPB).build();
    }

    public static List<String> convertConcursFaraIdPB_ToList(Protobuf.ConcursFaraIdPB concursFaraIdPB) {
        List<String> list = new ArrayList<>();
        list.add(concursFaraIdPB.getProba());
        list.add(concursFaraIdPB.getCategorie());
        return list;
    }

    public static Protobuf.ConcursFaraIdPB convertConcursDTO_ToProtoFaraId(String proba, String categorie) {
        return Protobuf.ConcursFaraIdPB.newBuilder()
                .setProba(proba)
                .setCategorie(categorie).build();
    }

    public static Protobuf.InscriereFaraIdPB convertInscriereDTO_ToProtoFaraId(Integer id_participant, Integer id_concurs) {
        return Protobuf.InscriereFaraIdPB.newBuilder()
                .setIdParticipant(id_participant)
                .setIdConcurs(id_concurs).build();
    }

    public static ConcursDTO convertConcurs_ToConcursDTO(Concurs concurs) {
        return new ConcursDTO(concurs.getId(), concurs.getProba(), concurs.getCategorie());
    }

    public static ParticipantDTO convertParticipant_ToParticipantDTO(Participant participant) {
        return new ParticipantDTO(participant.getId(), participant.getLastName(), participant.getFirstName(), participant.getAge());
    }

    public static Protobuf.InscriereFullPB convertInscriereDTO_ToInscriereFullPB1(InscriereDTO inscriere) {
//        Protobuf.ParticipantPB participantPB = convertParticipantDTO_ToProto(convertParticipant_ToParticipantDTO(inscriere.getParticipant()));
//        Protobuf.ConcursPB concursPB = convertConcursDTO_ToProto(convertConcurs_ToConcursDTO(inscriere.getConcurs()));
//
//        return Protobuf.InscriereFullPB.newBuilder()
//                .setId(inscriere.getId())
//                .setParticipant(participantPB)
//                .setConcurs(concursPB).build();
        Protobuf.InscriereFullPB inscriereFullPB = Protobuf.InscriereFullPB.newBuilder()
                .setId(0)
                .setParticipant(convertParticipantDTO_ToProto(convertParticipant_ToParticipantDTO(inscriere.getParticipant())))
                .setConcurs(convertConcursDTO_ToProto(convertConcurs_ToConcursDTO(inscriere.getConcurs())))
                .build();
        return inscriereFullPB;
    }

    public static Protobuf.Response updateInscriere(Inscriere inscriere) {
//        Protobuf.InscrierePB inscrierePB = convertInscriereDTO_ToInscrierePB(convertInscriere_ToInscriereDTO(inscriere));
//
//        Protobuf.Response response = Protobuf.Response.newBuilder()
//                .setType(Protobuf.Response.ResponseType.UPDATE)
//                .setInscriere(inscrierePB)
//                .build();
//
//        return response;
        Protobuf.InscriereFullPB inscriereFullPB = Protobuf.InscriereFullPB.newBuilder()
                .setId(inscriere.getId())
                .setParticipant(convertParticipantDTO_ToProto(convertParticipant_ToParticipantDTO(inscriere.getParticipant())))
                .setConcurs(convertConcursDTO_ToProto(convertConcurs_ToConcursDTO(inscriere.getConcurs())))
                .build();
        Protobuf.Response response = Protobuf.Response.newBuilder()
                .setType(Protobuf.Response.ResponseType.UPDATE)
                .setInscriereFull(inscriereFullPB)
                .build();
        return response;
    }

//TODO
//  Xx  LOGIN,
//  Xx  LOGOUT,
//  Xx  FIND_ACCOUNT_ORGANIZATOR,
//  Xx  FIND_PARTICIPANT_NUME_PRENUME_VARSTA,
//  Xx  FIND_ALL_PARTICIPANTS,
//    FILTER_PROBA_CATEGORIE_PARTICIPANT,
//  X  FIND_CONCURS_PROBA_CATEGORIE,
//  X  NUMAR_PROBE_PENTRU_PARTICIPANT,
//  X  SAVE_INSCRIERE,
//  X  SAVE_PARTICIPANT,
//  X  SAVE_CONCURS,
//  X  CREATE_AND_SAVE_CONCURS,
//  X  CREATE_AND_SAVE_PARTICIPANT,
//  X  FIND_INSCRIERE_PERS_CONC

    public static Protobuf.Request loginRequest(OrganizatorDTO organizator) {
        Protobuf.OrganizaotrPB organizatorPB = convertOrganizatorDTO_ToProto(organizator);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.LOGIN)
                .setOrg(organizatorPB).build();

        return request;
    }

    public static Protobuf.Request logoutRequest(OrganizatorDTO organizator) {
        Protobuf.OrganizaotrPB organizatorPB = convertOrganizatorDTO_ToProto(organizator);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.LOGOUT)
                .setOrg(organizatorPB).build();

        return request;
    }

    public static OrganizatorDTO findAccountOrganizator(Protobuf.Response response){
        Protobuf.OrganizaotrPB organizatorPB = response.getOrg();
        OrganizatorDTO organizatorDTO = new OrganizatorDTO(organizatorPB.getLastName(), organizatorPB.getFirstName(), organizatorPB.getParola());
        return organizatorDTO;
    }

    public static Protobuf.Request findAccountOrganizatorRequest(OrganizatorDTO organizator) {
        Protobuf.OrganizaotrPB organizatorPB = convertOrganizatorDTO_ToProto(organizator);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.FIND_ACCOUNT_ORGANIZATOR)
                .setOrg(organizatorPB).build();

        return request;
    }

    public static Protobuf.Request findParticipantNumePrenumeVarstaRequest(String nume, String prenume, Integer varsta) {
        Protobuf.ParticipantFaraIdPB participantFaraIdPB = convertParticipantDTO_ToProtoFaraId(nume, prenume, varsta);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.FIND_PARTICIPANT_NUME_PRENUME_VARSTA)
                .setParticipantFaraId(participantFaraIdPB).build();

        return request;
    }

    public static Protobuf.Request findAllParticipant() {
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.FIND_ALL_PARTICIPANTS)
                .build();

        return request;
    }

    public static List<ParticipantDTO> getParticipantsResponse(Protobuf.Response response) {
        List<ParticipantDTO> participantDTOList = new ArrayList<>();
        for(int i=0; i<response.getParticipantListCount(); i++) {
            Protobuf.ParticipantPB participantPB = response.getParticipantList(i);
            ParticipantDTO participantDTO = new ParticipantDTO(participantPB.getId(), participantPB.getLastName(), participantPB.getFirstName(), participantPB.getAge());
            participantDTOList.add(participantDTO);
        }

        return participantDTOList;
    }

    public static ParticipantDTO getParticipantResponse(Protobuf.Response response) {
        Protobuf.ParticipantPB participantPB = response.getParticipant();
        ParticipantDTO participantDTO = new ParticipantDTO(participantPB.getId(), participantPB.getLastName(), participantPB.getFirstName(), participantPB.getAge());
        return participantDTO;
    }

    public static Protobuf.Request filterProbaCateforieParticipantRequest(String proba, String categorie) {
        Protobuf.ConcursFaraIdPB concursFaraIdPB = convertConcursDTO_ToProtoFaraId(proba, categorie);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.FILTER_PROBA_CATEGORIE_PARTICIPANT)
                .setConcursFaraId(concursFaraIdPB).build();

        return request;
    }


    public static Protobuf.Request saveConcursRequest(ConcursDTO concurs) {
        Protobuf.ConcursPB concursPB = convertConcursDTO_ToProto(concurs);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.SAVE_CONCURS)
                .setConcurs(concursPB).build();

        return request;
    }

    public static Integer getNumarProbePentruParticipantResponse(Protobuf.Response response) {
        return response.getNumarProbePentruParticipanti();
    }

    public static Protobuf.Request numarProbePentruParticipantParticipantRequest(int id) {
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.NUMAR_PROBE_PENTRU_PARTICIPANT)
                .setNumarProbePentruParticipanti(id).build();

        return request;
    }


    public static Protobuf.Request saveParticipantRequest(ParticipantDTO participant) {
        Protobuf.ParticipantPB participantPB = convertParticipantDTO_ToProto(participant);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.SAVE_PARTICIPANT)
                .setParticipant(participantPB).build();

        return request;
    }

    public static Protobuf.Request needCreateParticipantRequest(String nume, String prenume, Integer varsta) {
        Protobuf.ParticipantFaraIdPB participantFaraIdPB = convertParticipantDTO_ToProtoFaraId(nume, prenume, varsta);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.CREATE_AND_SAVE_PARTICIPANT)
                .setParticipantFaraId(participantFaraIdPB).build();

        return request;
    }


    public static Inscriere saveInscriereResponse(Protobuf.Response response){
        Protobuf.InscriereFullPB inscrierePB = response.getInscriereFull();
        Inscriere inscriere = new Inscriere(convertParticipantDTO_ToParticipant(convertParticipantPB_ToParticipantDTO(inscrierePB.getParticipant())), convertConcursDTO_ToConcurse(convertConcursPB_ToConcursDTO(inscrierePB.getConcurs())));
        inscriere.setId(inscrierePB.getId());
        return inscriere;
    }

    public static Protobuf.Request findConcursProbaCategorie(String proba, String categorie) {
        Protobuf.ConcursFaraIdPB concursFaraIdPB = convertConcursDTO_ToProtoFaraId(proba, categorie);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.FIND_CONCURS_PROBA_CATEGORIE)
                .setConcursFaraId(concursFaraIdPB).build();

        return request;
    }

    public static ConcursDTO saveConcursResponse(Protobuf.Response response) {
        Protobuf.ConcursPB concursPB = response.getConcurs();
        ConcursDTO concursDTO = new ConcursDTO(concursPB.getId(), Proba.valueOf(concursPB.getProba()), Categorie.valueOf(concursPB.getCategorie()));
        return concursDTO;
    }

    public static Protobuf.Request createAnSaveConcursRequest(String proba, String categorie) {
        Protobuf.ConcursFaraIdPB concursFaraIdPB = convertConcursDTO_ToProtoFaraId(proba, categorie);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.CREATE_AND_SAVE_CONCURS)
                .setConcursFaraId(concursFaraIdPB).build();

        return request;
    }

    public static InscriereDTO saveInscriere(Protobuf.Response response){
        Protobuf.InscriereFullPB inscrierePB = response.getInscriereFull();
        InscriereDTO inscriereDTO = new InscriereDTO(inscrierePB.getId(), convertParticipantDTO_ToParticipant(convertParticipantPB_ToParticipantDTO(inscrierePB.getParticipant())), convertConcursDTO_ToConcurse(convertConcursPB_ToConcursDTO(inscrierePB.getConcurs())));
        return inscriereDTO;
    }

    public static Protobuf.Request saveInscriereRequest(InscriereDTO inscriereDTO) {
//        Protobuf.ConcursPB concursPB = convertConcursDTO_ToProto(convertConcurs_ToConcursDTO(inscriereDTO.getConcurs()));
//        Protobuf.ParticipantPB participantPB = convertParticipantDTO_ToProto(convertParticipant_ToParticipantDTO(inscriereDTO.getParticipant()));
//        Protobuf.Request request = Protobuf.Request.newBuilder()
//                .setType(Protobuf.Request.RequestType.SAVE_INSCRIERE)
//                .setConcurs(concursPB)
//                .setParticipant(participantPB).build();
//
//        return request;
        Protobuf.InscriereFullPB inscriereFaraIdPB = convertInscriereDTO_ToInscriereFullPB1(inscriereDTO);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.SAVE_INSCRIERE)
                .setInscriereFull(inscriereFaraIdPB).build();

        return request;
    }

    public static ConcursDTO convertConcursPB_ToConcursDTO(Protobuf.ConcursPB concursPB) {
        return new ConcursDTO(concursPB.getId(), Proba.valueOf(concursPB.getProba()), Categorie.valueOf(concursPB.getCategorie()));
    }

    public static ParticipantDTO convertParticipantPB_ToParticipantDTO(Protobuf.ParticipantPB participantPB) {
        return new ParticipantDTO(participantPB.getId(), participantPB.getLastName(), participantPB.getFirstName(), participantPB.getAge());
    }

    public static Concurs convertConcursDTO_ToConcurse(ConcursDTO concursDTO) {
        Concurs concurs = new Concurs(concursDTO.getCategorie(), concursDTO.getProga());
        concurs.setId(concursDTO.getId());
        return concurs;
    }

    public static Participant convertParticipantDTO_ToParticipant(ParticipantDTO participantDTO) {
        Participant participant = new Participant(participantDTO.getLastName(), participantDTO.getFirstName(), participantDTO.getAge());
        participant.setId(participantDTO.getId());
        return participant;
    }

    public static InscriereDTO findInscrierePersConc(Protobuf.Response response){
        Protobuf.InscriereFullPB inscrierePB = response.getInscriereFull();
        InscriereDTO inscriereDTO = new InscriereDTO(inscrierePB.getId(), convertParticipantDTO_ToParticipant(convertParticipantPB_ToParticipantDTO(inscrierePB.getParticipant())), convertConcursDTO_ToConcurse(convertConcursPB_ToConcursDTO(inscrierePB.getConcurs())));
        return inscriereDTO;
    }

    public static Protobuf.Request findInscrierePersConcRequest(int idParticipant, int idConcurs) {
        Protobuf.InscriereFaraIdPB inscriereFaraIdPB = convertInscriereDTO_ToProtoFaraId(idParticipant, idConcurs);
        Protobuf.Request request = Protobuf.Request.newBuilder()
                .setType(Protobuf.Request.RequestType.FIND_INSCRIERE_PERS_CONC)
                .setInscriereFaraId(inscriereFaraIdPB).build();

        return request;
    }
//TODO
// X   OK,
//  X  SAVE_INSCRIERE,
//  X  ERROR,
//  X  UPDATE,
//  X  NEED_CREATE_CONCURS,
//  X  NEED_CREATE_PARTICIPANT,

    public static ParticipantDTO need_create_participantResponse(Protobuf.Response response){
        Protobuf.ParticipantPB participantPB = response.getParticipant();
        ParticipantDTO participantDTO = new ParticipantDTO(participantPB.getId(), participantPB.getLastName(), participantPB.getFirstName(), participantPB.getAge());
        return participantDTO;
    }
}
