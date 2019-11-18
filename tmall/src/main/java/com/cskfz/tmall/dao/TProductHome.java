package com.cskfz.tmall.dao;
// default package
// Generated 2019-9-27 10:53:04 by Hibernate Tools 4.3.5.Final

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cskfz.tmall.entity.TProduct;



/**
 * Home object for domain model class TProduct.
 * @see .TProduct
 * @author Hibernate Tools
 */
@Repository
@Transactional
public class TProductHome {

	private static final Log log = LogFactory.getLog(TProductHome.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void persist(TProduct transientInstance) {
		log.debug("persisting TProduct instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TProduct instance) {
		log.debug("attaching dirty TProduct instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TProduct instance) {
		log.debug("attaching clean TProduct instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TProduct persistentInstance) {
		log.debug("deleting TProduct instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TProduct merge(TProduct detachedInstance) {
		log.debug("merging TProduct instance");
		try {
			TProduct result = (TProduct) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TProduct findById(int id) {
		log.debug("getting TProduct instance with id: " + id);
		try {
			TProduct instance = (TProduct) sessionFactory.getCurrentSession().get("com.cskfz.tmall.entity.TProduct", id);
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

	public List findByExample(TProduct instance) {
		log.debug("finding TProduct instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("com.cskfz.tmall.entity.TProduct").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List findByExample(int begin, int rows) {
		log.debug("finding TUser instance by example");
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria("com.cskfz.tmall.entity.TProduct");
			criteria.setFirstResult(begin);
			criteria.setMaxResults(rows);
			List results = criteria.list();	
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public int getTotal() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria("com.cskfz.tmall.entity.TProduct");
		//计算总数
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.uniqueResult()).intValue();
	}
	
}
