/* Icaro Cloud Simulator (ICLOS).
   Copyright (C) 2015 DISIT Lab http://www.disit.org - University of Florence

   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. */

package org.cloudsimulator.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Target;

@Target({ TYPE, METHOD, FIELD, PARAMETER })
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectedConfiguration {
    /**
     * Bundle key
     * 
     * @return a valid bundle key or ""
     */
    @Nonbinding
    String key() default "";

    /**
     * Is it a mandatory property
     * 
     * @return true if mandator
     */
    @Nonbinding
    boolean mandatory() default false;

    /**
     * Default value if not provided
     * 
     * @return default value or ""
     */
    @Nonbinding
    String defaultValue() default "";
}
