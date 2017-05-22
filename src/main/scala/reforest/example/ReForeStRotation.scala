/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reforest.example

import reforest.rf.rotation.RFRunnerRotation
import reforest.rf.RFProperty
import reforest.util.CCProperties

object ReForeStRotation {
  def main(args: Array[String]): Unit = {

    val property = new RFProperty(new CCProperties("ReForeSt", args(0)).load().getImmutable)

    val rfRunner = RFRunnerRotation.apply(property)

    val trainingData = rfRunner.loadData(0.7)

    val model = rfRunner.trainClassifier(trainingData)

    val labelAndPreds = rfRunner.getTestData().map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    val testErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / rfRunner.getTestData().count()
    rfRunner.printTree()
    rfRunner.sparkStop()

    println("Test Error = " + testErr)
  }
}
