/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;
import re.nectar.lib.cereal.Cerealizable;

/**
 * This interface is implemented by classes which provide a method to cerealize and uncerealize an object.
 * It is not needed that the object implements {@link Cerealizable} interface which purpose is for auto-cerializable objects
 *
 * @param <T> concrete class that this cerializer is able to handle
 */
public interface Cerealizer<T> {

    /**
     * Cerealize an object to a byte array
     * @param ba the target byte array
     * @param obj the object to cerealize
     */
    void cerealizeTo(ByteArray ba, T obj);

    /**
     * Uncerealize an object from a byte array
     * @param ba the source byte array
     */
    T uncerealizeFrom(ByteArray ba);

}
