package backend;

import Machine.Machine;
import jaxb.schema.generated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class BackEndMain {
Machine myEnigma;


    public static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc=JAXBContext.newInstance("enigma.jaxb.schema.generated");
        Unmarshaller U = jc.createUnmarshaller();
        return (CTEEnigma) U.unmarshal(in);
    }

    public static void setXmlData() throws JAXBException {
        try{
            InputStream inputStream = new FileInputStream(new File("src/ex1-sanity-small.xml"));
            CTEEnigma rotors = deserializeFrom(inputStream);
            System.out.println("abcd  of first rotor"+rotors.getCTEMachine().getABC().toString());
            System.out.println("notch of first rotor"+rotors.getCTEMachine().getCTERotors().getCTERotor().get(0).getNotch());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
