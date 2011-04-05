package eu.wisebed.wiseui.server.dao;

import eu.wisebed.wiseui.server.domain.BinaryImageBo;
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
