/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.maven.m2settings.template;

import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.interpolation.AbstractValueSource;
import org.codehaus.plexus.interpolation.Interpolator;
import org.jetbrains.annotations.NonNls;
import org.sonatype.nexus.client.core.NexusClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base URL {@link TemplateInterpolatorCustomizer}.
 *
 * @since 1.4
 */
@Component(role=TemplateInterpolatorCustomizer.class, hint="baseurl", instantiationStrategy="per-lookup")
public class BaseUrlCustomizer
    implements TemplateInterpolatorCustomizer
{
    @NonNls
    public static final String baseUrl = "baseUrl";

    private NexusClient nexusClient;

    // Constructor for Plexus
    public BaseUrlCustomizer() {
        super();
    }

    @VisibleForTesting
    public BaseUrlCustomizer(final NexusClient nexusClient)
    {
        this.nexusClient = checkNotNull(nexusClient);
    }

    @Override
    public void customize(final NexusClient client, final Interpolator interpolator) {
        this.nexusClient = checkNotNull(client);
        checkNotNull(interpolator);

        interpolator.addValueSource(new AbstractValueSource(false)
        {
            @Override
            public Object getValue(String expression) {
                String result = null;
                if (expression.equalsIgnoreCase(baseUrl)) {
                    result = getBaseUrl();
                }
                return result;
            }
        });
    }

    private String getBaseUrl() {
        return nexusClient.getConnectionInfo().getBaseUrl().toUrl();
    }
}

