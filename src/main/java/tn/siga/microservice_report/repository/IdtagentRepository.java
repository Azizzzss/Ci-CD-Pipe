package tn.siga.microservice_report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.siga.microservice_report.domain.Idtagent;
import tn.siga.microservice_report.service.dto.*;
import java.util.List;

@Repository
public interface IdtagentRepository  extends JpaRepository <Idtagent,Long> {
    @Query(nativeQuery = true,value ="select m.idt_matag as idtmatag,sum( MVP_MNT) mnt " +
            "from mvtpaie m, idtagent a " +
            "where ann_an = :p_annee and nmm_code = :p_mois and SENS=1  " +
            "and a.idt_corps=nvl(:p_crps,a.idt_corps) " +
            "and a.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util) " +
            " and ufg=:p_ufg and m.idt_matag=a.idt_matag " +
            " and ind_code <> 99999 " +
            "group by m.idt_matag " +
            "minus " +
            " select m.idt_matag  as idtmatag ,sum(mvp_mnt) as mnt from mvtpaie m,idtagent a " +
            "where ann_an = :p_annee and nmm_code = :p_mois and SENS=2 " +
            "and a.idt_corps=nvl(:p_crps,a.idt_corps) " +
            "and a.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util) " +
            " and ufg=:p_ufg and m.idt_matag=a.idt_matag " +
            "and ind_code <> 100220 " +
            " group by m.idt_matag ")


    List<DifNetDTO> getListDifNet(
            @Param("p_annee") Integer p_annee,
            @Param("p_mois") Integer p_mois,
            @Param("p_crps") String p_crps,
            @Param("p_util") String p_util,
            @Param("p_ufg") String p_ufg
    );


    @Query(value = "  select nvl(sum(mvp_mnt),0)   from mvtpaie where ann_an = :annee and nmm_code = :mois and SENS =2 and  idt_matag = :idt_matag ",nativeQuery = true)
    public Double getSumPaie(  @Param("annee") Integer annee,
                               @Param("mois") Integer mois, @Param("idt_matag") Integer idtmatag);



    @Query(nativeQuery = true, value = "select m.idt_matag as idtmatag,sum( MVP_MNT) mnt " +
            "from mvtpaie m, idtagent a " +
            "where ann_an = :p_annee and nmm_code = :p_nmm and ufg=nvl(:p_ufg,ufg) " +
            "and a.idt_corps=nvl(:p_crps,a.idt_corps)" +
            "and a.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util)" +
            "and ufg in(select code_ufg from ufg where cod_gufg=:p_gufg)" +
            "and  m.idt_matag=a.IDT_MATAG GROUP BY m.idt_matag")

    List<DifNetDTO> getListBullNeg( @Param("p_annee") Integer p_annee,
                                    @Param("p_nmm") Integer p_nmm,
                                    @Param("p_ufg") String p_ufg,
                                    @Param("p_crps") String p_crps,
                                    @Param("p_util") String p_util,
                                    @Param("p_gufg") Integer p_gufg
    );

    @Query(value = " SELECT IDT_NOMAG || ' ' || IDT_PRNAG AS CONCATENATED_NAME FROM idtagent WHERE idt_matag = :p_idt_matag ",nativeQuery = true)
    public String getNomAgent( @Param("p_idt_matag") Integer idtmatag);

    @Query(value = "select CORPS_DSG from corps where CORPS_CODE=:p_crps",nativeQuery = true)
    public String getDsgCore (  @Param("p_crps") String crps);


    @Query(value = "select LIB_GUFG from GROUPE_UFG where COD_GUFG =:p_code",nativeQuery = true)
    public String getLibGufg (  @Param("p_code") Integer code);

    @Query(value = "select LIB_UFG from ufg where CODE_UFG =:p_code",nativeQuery = true)
    public String getLibUfg (  @Param("p_code") String code);


    @Query(nativeQuery = true, value = "SELECT ALL abs.IDT_MATAG as idtmatag, agne.IDT_NOMAG||' '|| agne.IDT_PRNAG as nomPrenom, abs.TYP_ABS as typAbsc," +
            " conj.TYC_DSG as typCong, abs.SENS as sens, abs.NBH as nbHeure , abs.NB_JOUR as nbJours, abs.REF_ANNEE as refAnnee, abs.REF_MOIS as refMois " +
            "FROM ABSENCE abs , TYPCONG conj, IDTAGENT agne " +
            " WHERE (   (abs.IDT_MATAG = agne.IDT_MATAG) AND (abs.TYP_ABS = conj.TYC_TYPE)  )" +
            " and ann_an=:p_annee and nmm_code=:p_mois and abs.idt_matag=nvl(:p_matricule,abs.idt_matag) ORDER BY 1" )

    List<AgentDTO> getCtrlAbscence(@Param("p_annee") Integer p_annee,
                                   @Param("p_mois") Integer p_mois,
                                   @Param("p_matricule") Integer p_matricule);



//////////// etat comparatuf par rubrique : comp_rub.rdf
    @Query(nativeQuery = true, value="select ag.idt_matag as idtmatag,ag.IDT_NOMAG || ' ' || ag.IDT_PRNAG AS fullname  from idtagent ag where ag.IDT_MATAG in (select b.idt_matag as idtmatag  from mvtpaie a , idtagent b " +
            "where ann_an =:p_annee and nmm_code=:p_mois and ind_code=:p_code  and b.idt_corps=nvl(:p_crps,b.idt_corps)" +
            "     and b.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util)  and b.idt_matag=a.idt_matag " +
            "group by b.idt_matag " +
            "union " +
            "select b.idt_matag as idtmatag    from mvtpaie a , idtagent b " +
            "where ann_an=:p_annee1 and nmm_code=:p_mois1 and ind_code=:p_code  and b.idt_corps=nvl(:p_crps,b.idt_corps)" +
            " and b.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util)  and b.idt_matag=a.idt_matag" +
            " group by b.idt_matag " +
            "minus " +
            "select b.idt_matag as idtmatag  from mvtpaie a , idtagent b " +
            "where ann_an =:p_annee and nmm_code=:p_mois and ind_code=:p_code and b.idt_corps=nvl(:p_crps,b.idt_corps) " +
            "  and b.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util) and b.idt_matag=a.idt_matag " +
            "  group by b.idt_matag)")

    List<CompRubrDTO> getCompRub(@Param("p_annee") Integer p_annee,
                                 @Param("p_mois") Integer p_mois,
                                 @Param("p_code") Integer p_code,
                                 @Param("p_util") String p_util,
                                 @Param("p_crps") String p_crps,
                                 @Param("p_annee1") Integer p_annee1,
                                 @Param("p_mois1") Integer p_mois1);


    @Query(value = " select sum(DECODE(SENS,1,MVP_MNT,2,-MVP_MNT,mvp_mnt)) as somm1  from mvtpaie " +
            "  where ind_code = :p_code and ann_an = :p_annee and nmm_code = :p_mois and idt_matag = :p_idtmatag ",nativeQuery = true)
    public Double getSumPeriod( @Param("p_annee") Integer p_annee,
                                 @Param("p_mois") Integer p_mois,
                                 @Param("p_idtmatag") Integer p_idtmatag,
                                 @Param("p_code") Integer p_code);


    //get indemnité nom par code
    @Query(value = " select ind_code||' : '||InD_dsg as indemDsg from indem where ind_code = :p_code ",nativeQuery = true)
    public String getIndemDsg (  @Param("p_code") Integer p_code);

/// etat comp_agent.rdf
    @Query(nativeQuery = true, value = " select IND_CODE as indemCode ,null MVP_DATECH  from mvtpaie " +
            " where ann_an = :p_annee and nmm_code = :p_mois and idt_matag = :p_matricule and ind_code not in(1032,1033,1024,60,248,99995,1002,100006,100008,900050,900060,100104) " +
            " union " +
            " select IND_CODE,null MVP_DATECH  " +
            " from mvtpaie " +
            " where ann_an = :p_annee2 and nmm_code = :p_mois2 and idt_matag = :p_matricule and ind_code not in(1032,1033,1024,60,248,99995,1002,100006,100008,900050,900060,100104) " +
            "and to_char(IND_CODE)||to_char(IND_TYPE)  not in  " +
            "    (select to_char(IND_CODE)||to_char(IND_TYPE)  " +
            "        from  mvtpaie  " +
            "        where ann_an = :p_annee and nmm_code = :p_mois and ind_code not in(1032,1033,1024,60,248,99995,1002,100006,100008,900050,900060,100104) " +
            "        and idt_matag = :p_matricule) " +
            "order by indemCode ")
    List<CompAgentDTO> getCompAgent(@Param("p_annee") Integer p_annee,
                                 @Param("p_mois") Integer p_mois,
                                 @Param("p_matricule") Integer p_matricule,
                                 @Param("p_annee2") Integer p_annee2,
                                 @Param("p_mois2") Integer p_mois2);


    @Query(value = " select sum(DECODE(SENS,1,MVP_MNT,2,-MVP_MNT,mvp_mnt)) as somm1 " +
            "  from mvtpaie " +
            "  where ind_code = :p_code and ann_an = :p_annee and nmm_code = :p_mois and idt_matag = :p_idtmatag ",nativeQuery = true)
    public Double getSumPeriod_2( @Param("p_annee") Integer p_annee,
                                @Param("p_mois") Integer p_mois,
                                @Param("p_idtmatag") Integer p_idtmatag,
                                @Param("p_code") Integer p_code);

    @Query(value = " select InD_dsg as indemDsg from indem where ind_code = :p_code ",nativeQuery = true)
    public String getIndemNom (  @Param("p_code") Integer p_code);

    // generate report comp_tot_ufg.rdf


    @Query(nativeQuery = true , value = "select indemCode , libIndem, mntPeriode1, nbrAgentPeriode1, nbrAgentPeriode2, mntPeriode2, nvl(mntPeriode1,0) - nvl(mntPeriode2,0) as ecart " +
            "from ( " +
            "select b.IND_CODE  as indemCode ,ind_dsg as libIndem, " +
            "    (select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a " +
            "        where a.ind_code = b.ind_code " +
            "        and a.ann_an = :p_annee and a.nmm_code = :p_mois) as mntPeriode1, " +
            "    (select count(distinct m.idt_matag)   from mvtpaie m,idtagent a  " +
            "        where  ann_an = :p_annee and m.nmm_code = :p_mois and m.ind_code = b.ind_code  " +
            "        and a.idt_corps=nvl(:p_crps,a.idt_corps)  " +
            "        and a.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util)  " +
            "        and a.idt_matag=m.idt_matag) as nbrAgentPeriode1,  " +
            "    (select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a " +
            "        where a.ind_code = b.ind_code " +
            "        and a.ann_an = :p_annee2 and a.nmm_code = :p_mois2) as mntPeriode2, " +
            "    (select count(distinct m.idt_matag)   from mvtpaie m,idtagent a where  ann_an = :p_annee2 and m.nmm_code = :p_mois2 and m.ind_code = b.ind_code  " +
            "        and a.idt_corps=nvl(:p_crps,a.idt_corps)  " +
            "        and a.idt_corps in(select CORPS from USERS_CORPS where USR_NAME=:p_util)  " +
            "        and a.idt_matag=m.idt_matag ) as nbrAgentPeriode2 " +
            "from indem b " +
            "where ind_code not in(99995,900050,900060,1002) " +
            "order by 1 ) " +
            "where nvl(mntPeriode1,0) - nvl(mntPeriode2,0) <> 0 ")

    List <CompTotDTO>  getCompTot (    @Param("p_annee") Integer p_annee, @Param("p_mois") Integer p_mois, @Param("p_crps") String p_crps,
                                       @Param("p_util") String p_util,
                                       @Param("p_annee2") Integer p_annee2,
                                       @Param("p_mois2") Integer p_mois2 );




    @Query(nativeQuery = true, value = " select montant , compte, libellet, debit , credit , nvl(debit,0) - nvl(credit,0) as solde " +
            "from ( " +
            "    SELECT  b.ind_compta as compte , sum(mvp_mnt) as montant , " +

            "        (select lib_cpt  as libellet from plan_comptable where num_cpt = b.ind_compta) as libellet, " +

            "        (select sum(mvp_mnt) as vmnt  " +
            "        from mvtpaie a,paie_mois b ,indem c " +
            "        where ann_an = :p_annee  " +
            "        and nmm_code = :p_mois  " +
            "        and ind_compta = b.ind_compta " +
            "        and a.sens = 1 " +
            "        and ufg=nvl(:p_ufg,ufg)  " +
            "        and ufg in(select code_ufg from ufg where cod_gufg=:p_gufg) " +
            "        and a.ann_an=b.annee  " +
            "        and a.nmm_code =b.mois  " +
            "        and a.ind_code = c.ind_code  " +
            "        and a.idt_matag = b.idt_matag ) as debit, " +

            "        (select sum(mvp_mnt)  " +
            "        from mvtpaie a,paie_mois b ,indem c " +
            "        where ann_an = :p_annee  " +
            "        and nmm_code = :p_mois  " +
            "        and ind_compta = b.ind_compta " +
            "        and a.sens = 2 " +
            "        and ufg=nvl(:p_ufg,ufg)  " +
            "        and ufg in(select code_ufg from ufg where cod_gufg=:p_gufg) " +
            "        and a.ann_an=b.annee  " +
            "        and a.nmm_code =b.mois  " +
            "        and a.ind_code = c.ind_code  " +
            "        and a.idt_matag = b.idt_matag) as credit " +

            "    FROM MVTPAIE a, indem b, paie_mois c , plan_comptable d " +

            "    where ann_an= :p_annee " +
            "    and nmm_code = :p_mois " +
            "    and a.ind_code = b.ind_code " +
            "    and ufg=nvl(:p_ufg,ufg)  " +
            "    and ufg in(select code_ufg from ufg where cod_gufg=:p_gufg) " +
            "    and a.ann_an=c.annee " +
            "    and a.nmm_code = c.mois " +
            "    and a.idt_matag = c.idt_matag " +
            "    group by b.ind_compta " +
            "    order by b.ind_compta ) " +
            "    where compte is not null " )
    List <ComptaPaieDTO>  getComptaPaie (  @Param("p_annee") Integer p_annee,
                                           @Param("p_mois") Integer p_mois,
                                           @Param("p_ufg") String p_ufg,
                                           @Param("p_gufg") Integer p_gufg);


    @Query(value = "select NMM_LIBFR from NOMMOIS where NMM_CODE =:p_codeMois",nativeQuery = true)
    public String getNomMois (  @Param("p_codeMois") Integer p_codeMois);


    /* select matricule, nomPrenom, indemCode , libIndem, mntPeriode1, mntPeriode2, nvl(mntPeriode1,0) - nvl(mntPeriode2,0) as ecart " +
            "             from ( " +
            "             select distinct k.idt_matag as matricule, IDT_NOMAG ||' '|| idt_prnag as nomPrenom, b.IND_CODE  as indemCode ,ind_dsg as libIndem, " +
            "                 (select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a " +
            "                     where a.ind_code = b.ind_code " +
            "                     and a.idt_matag = k.idt_matag " +
            "                     and a.ann_an = :p_annee and a.nmm_code = :p_mois) as mntPeriode1 ," +

            "                 (select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a " +
            "                     where a.ind_code = b.ind_code " +
            "                     and a.idt_matag = k.idt_matag " +
            "                     and a.ann_an = :p_annee2 and a.nmm_code = :p_mois2) as mntPeriode2 " +

            "             from mvtpaie k, indem b  ,idtagent c " +
            "             where k.ind_code = b.ind_code " +
            "             and k.idt_matag = c.idt_matag " +
            "             AND NVl(nat_rub,-1) = 1 " +
            "             and k.ind_code not in(99995,900050,900060,1002) ) " +
            "             where nvl(mntPeriode1,0) - nvl(mntPeriode2,0) <> 0 " +
            "             order by matricule,indemCode */

    @Query(nativeQuery = true , value = " select matricule, nomPrenom ,' '||GRH_TUN.getObsCompPaie(matricule, :p_mois ,:p_annee) as observation , indemCode , libIndem, mntPeriode1, mntPeriode2, nvl(mntPeriode1,0) - nvl(mntPeriode2,0) as ecart " +
            "                          from (     " +
            "                          select distinct k.idt_matag as matricule, ' '|| IDT_NOMAG ||' '|| idt_prnag ||' '  as nomPrenom, b.IND_CODE  as indemCode ,' '||ind_dsg as libIndem,    " +
            "                              (select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a    " +
            "                                  where a.ind_code = b.ind_code    " +
            "                                  and a.idt_matag = k.idt_matag    " +
            "                                  and a.ann_an = :p_annee and a.nmm_code = :p_mois) as mntPeriode1 ,   " +
            " " +
            "                              (select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a    " +
            "                                  where a.ind_code = b.ind_code    " +
            "                                  and a.idt_matag = k.idt_matag    " +
            "                                  and a.ann_an = :p_annee2 and a.nmm_code = :p_mois2) as mntPeriode2    " +

            "                          from mvtpaie k, indem b  ,idtagent c    " +
            "                          where k.ind_code = b.ind_code    " +
            "                          and k.idt_matag = c.idt_matag    " +
            "                          AND NVl(nat_rub,-1) = 1    " +
            "                          and k.ind_code not in(99995,900050,900060,1002) )    " +
            "                          where nvl(mntPeriode1,0) - nvl(mntPeriode2,0) <> 0    " +
            "                          order by matricule,indemCode ")
    List <ComparatifPaieDTO>  getComparatifPaie ( @Param("p_annee") Integer p_annee,
                                                    @Param("p_mois") Integer p_mois,
                                                    @Param("p_annee2") Integer p_annee2,
                                                    @Param("p_mois2") Integer p_mois2);



    @Query(nativeQuery = true , value =" select b.famille as Famille ,a.ind_code as Indcode ,nmm_code as Nmmcode ,ind_dsg  as Inddsg  , ann_an||' / '||nmm_code as Ddt ,abs(sum(decode(a.sens,1,mvp_mnt,-mvp_mnt))) as Montant " +
                                        "from mvtpaie a,indem b,idtagent c " +
                                        "where ann_an=:p_annee " +
                                        "and nmm_code<=:p_mois " +
                                        "and a.ind_code=b.ind_code " +
                                        "and a.idt_matag=c.idt_matag " +

                                        "and famille>0 " +
                                        "and ufg = nvl(:p_ufg,ufg) " +
                                        "group by b.famille,a.ind_code,ind_dsg,ann_an||' / '||nmm_code,nmm_code " +
                                        "order by b.famille,a.ind_code,nmm_code ")
    List <LivrePaieDTO>  getLivrePaie ( @Param("p_annee") Integer p_annee,
                                        @Param("p_mois") Integer p_mois,
                                        @Param("p_ufg") String p_ufg);



    //   a blan querry

@Query(nativeQuery = true , value = " select a.idt_matag as Idtmatag ,c.ordre as Ordre ,b.ind_code as Indcode ,idt_nomag||' '||idt_prnag  as Np,c.ind_dsg as Inddsg ,abs (sum (decode (b.sens,2,-mvp_mnt,mvp_mnt))) as Montant " +
        "from mvtpaie b,idtagent a,indem c  " +
        "where a.idt_matag = b.idt_matag " +
        "   and b.ind_code = c.ind_code and b.ann_an = :p_annee " +
        "   and b.nmm_code = :p_mois  " +
        "group by a.idt_matag,idt_nomag||' '||idt_prnag,c.ordre,b.ind_code,c.ind_dsg " +
        "order by c.ordre,a.idt_matag ")
    List <LivrePaieAffDto> getLivrePaieAff( @Param("p_annee") Integer p_annee,
                                            @Param("p_mois") Integer p_mois);



@Query(nativeQuery = true , value = " select substr(rib,1,2) bnk_code,' ' ||bnq_dsg,rib_bnq,sum(MVP_MNT)  montant,COUNT(a.idt_matag) nb  " +
        "  from mvtpaie a,recapaie b,banque c,paie_mois g  " +
        "  where a.ann_an = b.ann_an and a.nmm_code = b.nmm_code  " +
        "  and a.idt_matag = b.idt_matag and a.idt_matag=g.idt_matag  " +
        "  and code_soc=:p_gufg and g.ufg=nvl(:p_ufg,ufg)  " +
        "  " +
        "and to_char(c.bnq_code)=substr(rib,1,2)   " +
        "and a.ann_an = :p_annee and a.nmm_code = :p_mois and ind_code = 999999  " +
        "AND MVP_MNT > 0   " +
        "and g.annee = :p_annee and g.mois = :p_mois  " +
        "  group by substr(rib,1,2),bnq_dsg,rib_bnq  " +
        "  " +
        "order by 1  " +
        "  " +
        "  " +
        "  ")
    List<Recap_virementDTO> getrecapvirement(@Param("p_annee") Integer p_annee,
                                             @Param("p_mois") Integer p_mois ,
                                             @Param("p_ufg") Integer p_ufg,
                                             @Param("p_gufg") Integer p_gufg);


@Query(nativeQuery = true, value = " SELECT " +
        "    m.idt_matag AS matricule, " +
        "    a.IDT_NOMAG ||' '|| a.IDT_PRNAG as nomPrenom " +
        "FROM " +
        "    mvtpaie m, " +
        "    idtagent a " +
        "WHERE " +
        "    ann_an = :p_annee1 " +
        "    AND nmm_code = :p_mois1 " +
        "    AND idt_corps = NVL(:p_crps, idt_corps) " +
        "    AND idt_corps IN (SELECT CORPS FROM USERS_CORPS WHERE USR_NAME = :p_util) " +
        "    AND ind_code = 999999 " +
        "    AND sens = 2 " +
        "    AND m.idt_matag = a.idt_matag " +
        "GROUP BY " +
        "    m.idt_matag, " +
        "    a.IDT_NOMAG , " +
        "    a.IDT_PRNAG  " +
        " " +
        "MINUS " +
        " " +
        "SELECT " +
        "    m.idt_matag AS matricule, " +
        "    a.IDT_NOMAG ||' '|| a.IDT_PRNAG as nomPrenom " +
        "FROM " +
        "    mvtpaie m, " +
        "    idtagent a " +
        "WHERE " +
        "    ann_an = :p_annee2 " +
        "    AND nmm_code = :p_mois2 " +
        "    AND idt_corps = NVL(:crps, idt_corps) " +
        "    AND idt_corps IN (SELECT CORPS FROM USERS_CORPS WHERE USR_NAME = :p_util) " +
        "    AND ind_code = 999999 " +
        "    AND sens = 2 " +
        "    AND m.idt_matag = a.idt_matag " +
        "GROUP BY " +
        "    m.idt_matag, " +
        "    a.IDT_NOMAG, " +
        "    a.IDT_PRNAG   ")
List<Diff_agentDTO> getdiffagentquerry1(@Param("p_annee1") Integer p_annee,
                                        @Param("p_annee2") Integer p_annee2,
                                        @Param("p_mois") Integer p_mois,
                                        @Param("p_mois2") Integer p_mois2,
                                        @Param("p_crps") String p_crps,
                                        @Param("p_util") String p_util);
    



    @Query(nativeQuery = true, value = "   SELECT m.idt_matag AS matricule, " +
            "         a.IDT_NOMAG || ' ' || a.IDT_PRNAG     AS nomPrenom " +
            "    FROM mvtpaie m, idtagent a " +
            "   WHERE     ann_an = :p_annee1 " +
            "         AND nmm_code = :p_mois1 " +
            "         AND ind_code = 999999 " +
            "         AND sens = 2 " +
            "         AND idt_corps = NVL ( :p_crps, idt_corps) " +
            "         AND idt_corps IN (SELECT CORPS " +
            "                             FROM USERS_CORPS " +
            "                            WHERE USR_NAME = :p_util) " +
            "         AND m.idt_matag = a.idt_matag " +
            "GROUP BY m.idt_matag, a.IDT_NOMAG, a.IDT_PRNAG " +
            "MINUS " +
            "  SELECT m.idt_matag                           AS matricule, " +
            "         a.IDT_NOMAG || ' ' || a.IDT_PRNAG     AS nomPrenom " +
            "    FROM mvtpaie m, idtagent a " +
            "   WHERE     ann_an = :p_annee2 " +
            "         AND nmm_code = :p_mois2 " +
            "         AND idt_corps = NVL ( :p_crps, idt_corps) " +
            "         AND idt_corps IN (SELECT CORPS " +
            "                             FROM USERS_CORPS " +
            "                            WHERE USR_NAME = :p_util) " +
            "         AND ind_code = 999999 " +
            "         AND sens = 2 " +
            "         AND m.idt_matag = a.idt_matag  " +
            "GROUP BY m.idt_matag, a.IDT_NOMAG, a.IDT_PRNAG ")
    List<Diff_agentDTO> getdiffagentquerry2(@Param("p_annee1") Integer p_annee,
                                            @Param("p_annee2") Integer p_annee2,
                                            @Param("p_mois") Integer p_mois,
                                            @Param("p_mois2") Integer p_mois2,
                                            @Param("p_crps") String p_crps,
                                            @Param("p_util") String p_util
    );



    @Query(nativeQuery = true , value =" select a.idt_matag as idtmatag,idt_nomag||' '||idt_prnag as nomprenom,idt_categorie as idtcategorie ,idt_poste as idtposte,idt_cho_code as idtchocode,idt_emp_code as idtempcode ,idt_datrec as idtdatrec ,idt_datnais as idtdatnais ,ufa as ufa , " +
            "ind_code as indcode ,abs(sum(decode(sens,1,mvp_mnt,-mvp_mnt))) as montant  " +
            "from paie_mois a,mvtpaie b " +
            "where a.annee = b.ann_an " +
            "and a.mois=b.nmm_code " +
            "and a.idt_matag = b.idt_matag " +
            "and a.annee = :p_annee " +
            "and a.mois = :p_mois " +
            "and (b.ind_code <4999 or b.ind_code = 99999) " +
            "group by a.idt_matag,idt_nomag,idt_prnag,idt_categorie,idt_poste,idt_cho_code,idt_emp_code,idt_datrec,idt_datnais,ufa, " +
            "ind_code " +
            "order by a.idt_matag,b.ind_code ")
    List<Paie_genDto> getpaiegen( @Param("p_annee") Integer p_annee , @Param("p_mois") Integer p_mois );






    @Query(nativeQuery = true , value = "SELECT distinct  a.IDT_MATAG as idtmatag , b.IDT_PRNAG||' '||b.IDT_NOMAG  as nomprenom , idt_matas as idtmatas " +
            "FROM MVTPAIE a, IDTAGENT b " +
            "WHERE (a.ANN_AN = :p_annee " +
            " AND a.NMM_CODE = :p_mois " +
            " AND a.IND_CODE = 100220 and mvp_mnt >0) " +
            " AND (a.IDT_MATAG = b.IDT_MATAG) " +
            "ORDER BY 1 ")
    List<Dec_assurDTO> getdec_assur(@Param("p_annee") Integer p_annee , @Param("p_mois") Integer p_mois);




    @Query(nativeQuery = true , value = "SELECT distinct  a.IDT_MATAG as Idtmatag, b.IDT_PRNAG||' '||b.IDT_NOMAG as Nomprenom , idt_matas as Idtmatas " +
            "FROM MVTPAIE a, IDTAGENT b " +
            "WHERE (a.ANN_AN = :p_annee " +
            " AND a.NMM_CODE = :p_mois " +
            " AND a.IND_CODE = 100220 and mvp_mnt >0) " +
            " AND (a.IDT_MATAG = b.IDT_MATAG) " +
            "ORDER BY 1 ")
    List<Dec_impoDTO> getdec_impo(@Param("p_annee") Integer p_annee , @Param("p_mois") Integer p_mois );











}