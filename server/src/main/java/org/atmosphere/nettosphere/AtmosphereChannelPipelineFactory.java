/*
 * Copyright 2012 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.atmosphere.nettosphere;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

import static org.jboss.netty.channel.Channels.pipeline;

class AtmosphereChannelPipelineFactory implements
        ChannelPipelineFactory {

    private final transient NettyAtmosphereHandler nettyAtmosphereHandler;
    private final transient Config config;

    public AtmosphereChannelPipelineFactory(final NettyAtmosphereHandler nettyAtmosphereHandler) {
        this.nettyAtmosphereHandler = nettyAtmosphereHandler;
        config = nettyAtmosphereHandler.config();
    }

    /**
     * Retrieve the channel pipeline factory.
     *
     * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
     */
    public ChannelPipeline getPipeline() {
        final ChannelPipeline pipeline = pipeline();

        if (config.engine() != null) {
            pipeline.addLast("ssl", new SslHandler(config.engine()));
        }

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("nettyAtmosphereHandler", nettyAtmosphereHandler);
        return pipeline;
    }

}
