package ibd.web.DAO;

import ibd.web.DataObjects.IndexAnalysisRow;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class IndexAnalysisRowDAO {
	@Autowired private SessionFactory sessionFactory;
	
	/**
	 * @Transactional annotation below will trigger Spring Hibernate transaction manager to automatically create
	 * a hibernate session. See src/main/webapp/WEB-INF/servlet-context.xml
	 */
	@Transactional
	public List<IndexAnalysisRow> findAll() {
		Session session = sessionFactory.getCurrentSession();
		List pizzas = session.createQuery("from Pizza").list();
		return pizzas;
	}
}