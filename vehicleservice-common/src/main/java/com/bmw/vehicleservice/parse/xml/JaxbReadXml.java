package com.bmw.vehicleservice.parse.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

public class JaxbReadXml {

    @SuppressWarnings("unchecked")
    public static <T> T readString(Class<T> clazz, String context) throws JAXBException {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(new File(JaxbReadXml.class.getResource(context).getFile()));
        } catch (JAXBException e) {
            // logger.trace(e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readConfig(Class<T> clazz, String config, Object... arguments) throws IOException,
            JAXBException {
        InputStream is = null;
        try {
            if (arguments.length > 0) {
                config = MessageFormat.format(config, arguments);
            }
            // logger.trace("read configFileName=" + config);
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            is = new FileInputStream(config);
            return (T) u.unmarshal(is);
        } catch (IOException e) {
            // logger.trace(config, e);
            throw e;
        } catch (JAXBException e) {
            // logger.trace(config, e);
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readConfigFromStream(Class<T> clazz, InputStream dataStream) throws JAXBException {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(dataStream);
        } catch (JAXBException e) {
            // logger.trace(e);
            throw e;
        }
    }

//    public static void main(String[] args) throws JAXBException {
//    	StateMachineBuilder builder = JaxbReadXml.readString(StateMachineBuilder.class, "/command-policy.xml");
//        System.out.println(builder.getStateMachineList().toString());
//        for (StateMachine machine : builder.getStateMachineList()) {
//            System.out.println(machine.getStatesList().toString());
//            for(State state : machine.getStatesList()){
//            	 System.out.println(state.getServiceType());
//            	 System.out.println(state.getMessageType());
//            	 System.out.println(state.getCommandType());
//            	 System.out.println(state.getConditionCheck());
//            	 System.out.println(state.getConditionsList().toString());
//            	 for(Condition condition : state.getConditionsList()){
//            		 System.out.println("=================>typeId:"+condition.getTypeId());
//            		 if(condition.getTypeId()==1){
//            			 System.out.println("=================>newState:"+condition.getNewState());
//            			 System.out.println("=================>actionName:"+condition.getActionName());
// 					}else if(condition.getTypeId()==2){
// 						System.out.println("=================>newState:"+condition.getNewState());
// 						System.out.println("=================>actionName:"+condition.getActionName());
// 					}
//            	 }
//            }
//        }
//    }
    
}