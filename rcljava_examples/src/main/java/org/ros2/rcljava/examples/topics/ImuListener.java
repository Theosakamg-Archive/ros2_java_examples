/* Copyright 2016 Esteve Fernandez <esteve@apache.org>
 * Copyright 2016-2017 Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ros2.rcljava.examples.topics;

import org.ros2.rcljava.qos.QoSProfile;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.SubscriptionCallback;
import org.ros2.rcljava.node.topic.Subscription;

public class ImuListener {
    private static final String NODE_NAME = Listener.class.getSimpleName().toLowerCase();

    private static void imuCb(final sensor_msgs.msg.Imu msg)
    {
        System.out.println(String.format(" accel: [%+6.3f %+6.3f %+6.3f]\n",
            msg.getLinearAcceleration().getX(),
            msg.getLinearAcceleration().getY(),
            msg.getLinearAcceleration().getZ()));
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a new Node
        Node node = RCLJava.createNode(NODE_NAME);

        // Subscriptions are type safe, so we'll pass the message type.
        Subscription<sensor_msgs.msg.Imu> sub = node.<sensor_msgs.msg.Imu>createSubscription(
                sensor_msgs.msg.Imu.class,
                "imu",
                new SubscriptionCallback<sensor_msgs.msg.Imu>() {
                    // We define the callback inline, this works with Java 8's lambdas too, but we use
                    // our own Consumer interface because Android supports lambdas via retrolambda, but not
                    // the lambda API
                    @Override
                    public void dispatch(sensor_msgs.msg.Imu msg) {
                        ImuListener.imuCb(msg);
                    }
                },
                QoSProfile.SENSOR_DATA);

        RCLJava.spin(node);

        sub.dispose();
        node.dispose();
        RCLJava.shutdown();
    }

}
