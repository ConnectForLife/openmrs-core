/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.api.impl;

import org.hibernate.Session;
import org.openmrs.Concept;
import org.openmrs.api.CachedConceptService;
import org.openmrs.api.db.hibernate.DbSessionFactory;

public class CachedConceptServiceImpl implements CachedConceptService {

	private InternalCachedConceptService internalCachedConceptService;
	private DbSessionFactory dbSessionFactory;

	public void setInternalCachedConceptService(InternalCachedConceptService internalCachedConceptService) {
		this.internalCachedConceptService = internalCachedConceptService;
	}

	public void setDbSessionFactory(DbSessionFactory dbSessionFactory) {
		this.dbSessionFactory = dbSessionFactory;
	}

	@Override
	public Concept getConcept(int conceptId) {
		return ensureEntityInSession(internalCachedConceptService.getCachedConcept(conceptId));
	}

	@Override
	public Concept getConceptByUuid(String conceptUuid) {
		return ensureEntityInSession(internalCachedConceptService.getCachedConceptByUuid(conceptUuid));
	}

	private Concept ensureEntityInSession(Concept cachedValue) {
		final Session currentSession = getCurrentSession();

		if (currentSession.contains(cachedValue)) {
			return cachedValue;
		}

		return (Concept) currentSession.get(Concept.class, cachedValue.getId());
	}

	private Session getCurrentSession() {
		return dbSessionFactory.getHibernateSessionFactory().getCurrentSession();
	}
}
