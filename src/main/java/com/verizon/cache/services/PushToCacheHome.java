package com.verizon.cache.services;

// Generated Oct 7, 2016 9:53:07 PM by Hibernate Tools 3.4.0.CR1

import java.nio.file.Paths;
import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.service.ServiceRegistry;

import com.verizon.cache.domain.PushToCache;
import com.verizon.cache.domain.PushToCacheId;

/**
 * Home object for domain model class PushToCache.
 * @see com.verizon.cache.services.PushToCache
 * @author Hibernate Tools
 */
public class PushToCacheHome {

	private static final Log log = LogFactory.getLog(PushToCacheHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();
    private static ServiceRegistry serviceRegistry;

	
	protected SessionFactory getSessionFactory() {
		try {
		    Configuration configuration = new Configuration();
		    configuration.configure();
		    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
		            configuration.getProperties()).build();
		    return configuration.buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(
					e.toString());
		}
	}
	
	public void persist(PushToCache transientInstance) {
		log.debug("persisting PushToCache instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PushToCache instance) {
		log.debug("attaching dirty PushToCache instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PushToCache instance) {
		log.debug("attaching clean PushToCache instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PushToCache persistentInstance) {
		log.debug("deleting PushToCache instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PushToCache merge(PushToCache detachedInstance) {
		log.debug("merging PushToCache instance");
		try {
			PushToCache result = (PushToCache) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PushToCache findById(PushToCacheId id) {
		log.debug("getting PushToCache instance with id: " + id);
		try {
			PushToCache instance = (PushToCache) sessionFactory
					.getCurrentSession().get(
							"com.verizon.cache.services.PushToCache", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PushToCache instance) {
		log.debug("finding PushToCache instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.verizon.cache.services.PushToCache")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List getByHtmlName(String currentHtml) {
		
		String currentFileName = Paths.get(currentHtml).getFileName().toString();
		log.info("finding all entry for [" + currentFileName + "]");
		try {

			List results = sessionFactory
					.openSession()
					.createQuery(" From PushToCache where HtmlFileName = :currentFile" )
					.setParameter("currentFile", currentFileName)
					.list();
			log.info("GetByHtmlName successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("GetByHtmlName failed", re);
			throw re;
		}
	}
}
