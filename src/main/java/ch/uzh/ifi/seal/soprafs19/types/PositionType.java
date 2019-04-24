package ch.uzh.ifi.seal.soprafs19.types;

import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class PositionType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER};
    }

    @Override
    public Class returnedClass()
    {
        return Position.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException
    {
        int x = rs.getInt(names[0]);

        if (rs.wasNull())
            return null;

        int y = rs.getInt(names[1]);
        int z = rs.getInt(names[2]);
        Position position = new Position(x, y, z);

        return position;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException
    {

        if (Objects.isNull(value)) {
            st.setNull(index, Types.INTEGER);
        } else {
            Position position = (Position) value;
            st.setInt(index,position.getX());
            st.setInt(index+1,position.getY());
            st.setInt(index+2,position.getZ());
        }
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public boolean equals(Object x, Object y)
    {
        if (x == y) return true;
        if (!(x instanceof Position && y instanceof Position)) {
            return false;
        }
        x = (Position) x;
        y = (Position) y;

        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException
    {
        assert (x != null);
        return x.hashCode();
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException
    {
        return value;
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }
}
