package portfolio.dao;

import portfolio.dto.PortfolioDTO;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDAO {
    // 보유 코인 목록 조회
    public List<PortfolioDTO> selectAll(String userId) {
        List<PortfolioDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "select p.portfolio_id, p.asset_id, a.name, p.quantity, p.avg_price"
                + " from portfolio p join asset a on(p.asset_id = a.asset_id)"
                + " where p.user_id = ?"
                + " order by a.name";
        PortfolioDTO dto = null;
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, userId);
            rs = st.executeQuery();
            while (rs.next()) {
                dto = new PortfolioDTO();
                dto.setPortfolioId(rs.getInt("portfolio_id"));
                dto.setAssetId(rs.getString("asset_id"));
                dto.setName(rs.getString("name"));
                dto.setQuantity(rs.getInt("quantity"));
                dto.setAvgPrice(rs.getBigDecimal("avg_price"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return list;
    }
}