/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/


package com.gridnine.codeflow.api;

import com.gridnine.codeflow.tracer.Tracer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Context {
   private final Map<ContextKey<?>, Object> data = new HashMap<>();

   private Tracer tracer;

   public Tracer getTracer() {
      return tracer;
   }

   public void setTracer(Tracer tracer) {
      this.tracer = tracer;
   }

   private <T> T getData(ContextKey<T> key, boolean mandatory) {
      Object result = data.get(key);
      if(result == null){
         if(mandatory){
            throw new RuntimeException(String.format("mandatory object with id %s is absent in context", key.getId()));
         }
         return null;
      }
      if(!result.getClass().isAssignableFrom(key.getValueClass())){
         throw new RuntimeException(String.format("object with id %s has wrong type %s", key.getId(), result.getClass().getName()));
      }
      return (T) result;
   }

   public<T> T getData(ContextKey<T> key) {
      return getData(key, false);
   }

   public<T> T getMandatoryData(ContextKey<T> key) {
      return getData(key, true);
   }

   public<T> void setData(ContextKey<T> key, T data) {
      this.data.put(key, data);
   }

   public Set<ContextKey<?>> getAllDataKeys(){
      return data.keySet();
   }

}
