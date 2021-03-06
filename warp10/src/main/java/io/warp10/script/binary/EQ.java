//
//   Copyright 2016  Cityzen Data
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp10.script.binary;

import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptStackFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.math.BigDecimal;

/**
 * Checks the two operands on top of the stack for equality
 */
public class EQ extends NamedWarpScriptFunction implements WarpScriptStackFunction {

  public EQ(String name) {
    super(name);
  }
  
  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {
    Object op2 = stack.pop();
    Object op1 = stack.pop();
    
    if (op2 instanceof Double && op1 instanceof Double) {
      if (Double.isNaN((Double) op1) || Double.isNaN((Double) op2)) {
        stack.push(Double.isNaN((Double) op1) && Double.isNaN((Double) op2));
      } else {
        stack.push(0 == compare((Number) op1, (Number) op2));        
      }
    } else if (op2 instanceof Number && op1 instanceof Number) {
      stack.push(0 == compare((Number) op1, (Number) op2));
    } else if (op2 instanceof String && op1 instanceof String) {
      stack.push(op1.toString().equals(op2.toString()));
    } else if (op2 instanceof Boolean && op1 instanceof Boolean) {
      stack.push(op1.equals(op2));
    } else if (op2 instanceof RealVector && op1 instanceof RealVector) {
      stack.push(op1.equals(op2));
    } else if (op2 instanceof RealMatrix && op1 instanceof RealMatrix) {
      stack.push(op1.equals(op2));
    } else {
      throw new WarpScriptException(getName() + " can only operate on homogeneous numeric, string or boolean types.");
    }
    
    return stack;
  }
  
  public static int compare(Number a, Number b) {
    if (a.equals(b)) {
      return 0;
    }    
    return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
  }
}
