package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Booking;
import ba.unsa.etf.rpr.exceptions.MyException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class BookingDaoSQLImpl extends AbstractDao<Booking> implements BookingDao{


    public BookingDaoSQLImpl() {
        super("Booking");
    }

    @Override
    public List<Booking> searchByCustomerId(int id) throws IOException {
        String query = "SELECT * FROM Booking WHERE customer_id = ?";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Booking> bookingList = new ArrayList<>();
            while(rs.next()){
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setTicket_price(rs.getDouble("ticket_price"));
                b.setTour_id(TourDaoSQLImpl.getInstance().getById(rs.getInt("tour_id")));
                b.setCustomer_id(CustomerDaoSQLImpl.getInstance().getById(rs.getInt("customer_id")));
                bookingList.add(b);
            }
            rs.close();
            return bookingList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking row2object(ResultSet rs) throws MyException {
        try{
            Booking booking = new Booking();
            booking.setId(rs.getInt(1));
            booking.setTicket_price(rs.getDouble(2));
            booking.setTour_id(DaoFactory.tourDao().getById(rs.getInt(3)));
            booking.setCustomer_id(DaoFactory.customerDao().getById(rs.getInt(4)));
            return booking;
        } catch (Exception e) {
            throw new MyException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Booking object) {
        Map<String, Object> item = new TreeMap<>();
        item.put("id", object.getId());
        item.put("ticket_price", object.getTicket_price());
        item.put("tour_id", object.getTour_id());
        item.put("customer_id", object.getCustomer_id());
        return item;
    }

}
