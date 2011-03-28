package eu.wisebed.wiseui.server.manager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import eu.wisebed.wiseui.server.model.Image;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;

public class ImageServiceManager {

	/**
	 * Persist an image object 
	 * @param image
	 */
	public static final void saveImage(Image image){
		final Session session = WiseUiHibernateUtil.getSessionFactory()
			.getCurrentSession();
		session.beginTransaction();
		session.save(image);
		session.getTransaction().commit();
	}

	/**
	 * Fetch image by filename.
	 * @param filename
	 * @return image An image object
	 */
	public static Image fetchImageByFilename(final String filename){
		final Session session = WiseUiHibernateUtil.getSessionFactory().
			getCurrentSession();
		session.beginTransaction();

		Criteria criteria = session.createCriteria(Image.class).add(
				Restrictions.eq("imageFileName", filename));
		Image image = (Image) criteria.uniqueResult();
		return image;
	}
}
