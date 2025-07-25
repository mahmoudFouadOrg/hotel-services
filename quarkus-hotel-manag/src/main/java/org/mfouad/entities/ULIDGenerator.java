//package org.mfouad.entities;
//
//import java.io.Serializable;
//
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.hibernate.id.IdentifierGenerator;
//
//public class ULIDGenerator implements IdentifierGenerator {
//
//    @Override
//    public Serializable generate(SharedSessionContractImplementor session, Object object) {
//        return Ulid.random().toString();
//    }
//
//}
