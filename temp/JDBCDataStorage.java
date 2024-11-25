package be.iii.jdbclabo.data;

import be.iii.jdbclabo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCDataStorage implements IDataStorage {
    @Autowired
    private DataSource dataSource;

    @Value("${sqlopdrachten.select_products}")
    private String selectProductsQuery;

    @Override
    public List<IProduct> getProducts() throws DataExceptie {
        List<IProduct> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectProductsQuery);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                IProduct product = mapResultSetToProduct(resultSet);
                products.add(product);
            }

        } catch (SQLException e) {
            throw new DataExceptie("Couldn't fetch products.");
        }
        return products;
    }

    private IProduct mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getString("productCode"),
                resultSet.getString("productName"),
                resultSet.getString("productLine"),
                resultSet.getString("productScale"),
                resultSet.getString("productVendor"),
                resultSet.getString("productDescription"),
                resultSet.getInt("quantityInStock"),
                resultSet.getDouble("buyPrice"),
                resultSet.getDouble("msrp")
                );
    }

    @Override
    public List<ICustomer> getCustomers() throws DataExceptie {
        return null;
    }

    @Override
    public List<IOrderWithoutDetails> getOrders(int customerNumber) throws DataExceptie {
        return null;
    }

    @Override
    public int maxCustomerNumber() throws DataExceptie {
        return 0;
    }

    @Override
    public int maxOrderNumber() throws DataExceptie {
        return 0;
    }


    @Override
    public void addOrder(IOrder order) throws DataExceptie {

    }


    @Override
    public void addCustomer(ICustomer customer) throws DataExceptie {

    }


    @Override
    public void modifyCustomer(ICustomer customer) throws DataExceptie {
    }


    @Override
    public void deleteCustomer(int customerNumber) throws DataExceptie {
    }


    @Override
    public double getTotal(int customerNumber) throws DataExceptie {
        return 0;
    }
}
