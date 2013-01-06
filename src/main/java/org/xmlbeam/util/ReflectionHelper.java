/**
 *  Copyright 2013 Sven Ewald
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.xmlbeam.util;

import java.lang.reflect.Method;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="https://github.com/SvenEwald">Sven Ewald</a>
 */
public class ReflectionHelper {
    public static Set<Class<?>> findAllCommonSuperInterfaces(Class<?> a, Class<?> b) {
        Set<Class<?>> seta = new HashSet<Class<?>>(findAllSuperInterfaces(a));
        Set<Class<?>> setb = new HashSet<Class<?>>(findAllSuperInterfaces(b));
        seta.retainAll(setb);
        return seta;
    }

    public static Collection<? extends Class<?>> findAllSuperInterfaces(Class<?> a) {
        Set<Class<?>> set = new HashSet<Class<?>>();
        if (a.isInterface()) {
            set.add(a);
        }
        for (Class<?> i : a.getInterfaces()) {
            set.addAll(findAllSuperInterfaces(i));
        }
        return set;
    };

    public static boolean isSetter(final Method method) {
        return method.getName().toLowerCase().startsWith("set") && hasParameters(method);
    }

    public static boolean hasReturnType(final Method method) {
        if (method.getReturnType() == null) {
            return false;
        }
        if (Void.class.equals(method.getReturnType())) {
            return false;
        }

        return !Void.TYPE.equals(method.getReturnType());
    }

    public static boolean hasParameters(final Method method) {
        return (method.getParameterTypes().length > 0);
    }
}
