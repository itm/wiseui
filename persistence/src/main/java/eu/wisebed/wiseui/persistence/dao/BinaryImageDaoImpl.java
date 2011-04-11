package eu.wisebed.wiseui.persistence.dao;

import eu.wisebed.wiseui.persistence.domain.BinaryImageBo;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public class BinaryImageDaoImpl extends AbstractDaoImpl<BinaryImageBo> implements BinaryImageDao {

    public BinaryImageDaoImpl() {
        super(BinaryImageBo.class);
    }
}
