/**
 *     LTLTD - Repeat Java Utility - A small Java library for repeating things.
 *     Copyright (C) 2022 Littlethunder Limited/William Dixon
 *     code@ltltd.net
 *      <p>
 *      net.ltltd.util.repeat is a collection of utility methods which repeat things. It creates repetitive collections,
 *      invokes things multiple times, streams things repetitively, etc.
 *      </p>
 *      <p>
 *      In order to remain compact, and facilitate minimizing jar and runtime size, it imports a minimum of java classes
 *      and is entirely free of external dependencies.
 *      </p>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/gpl-3.0.txt
 */
module util_repeat {                //not open, no reflection permitted
    //requires <none>;              //no runtime dependencies
    //requires static <none>;       //no compiletime dependencies
    //requires transitive <none>;   //no transitive dependencies
    //uses <none>;                  //no external services consumed
    //provides <none>;              //export no abstract classes or interfaces,nor implementations via provides...with
    //opens <none>;                 //no reflective access, even conditionally via opens ... to
    exports net.ltltd.util.repeat;  //eveything we are - no restriction via exports ... to

}