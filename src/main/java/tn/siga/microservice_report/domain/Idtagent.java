package tn.siga.microservice_report.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Entity
@Table(name = "IDTAGENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Idtagent implements Serializable {


        @Id
        @Column(name = "IDT_MATAG")
        private Long idt_matag;

        @Column(name = "IDT_NOMAG")
        private String idt_nomag;

        @Column(name = "IDT_NOMAGAR")
        private String idt_nomagar;

        @Column(name = "IDT_PRNAG")
        private String idt_prnag;

        @Column(name = "IDT_PRNAGAR")
        private String idt_prnagar;

        @Column(name = "IDT_PRNPE")
        private String idt_prnpe;

        @Column(name = "UFG")
        private String ufg;

}
