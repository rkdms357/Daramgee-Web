package portfolio.dao;

import portfolio.dto.PortfolioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDAO {
    // 보유 코인 목록 조회
    public List<PortfolioDTO> selectAll(Connection conn, String userId) throws SQLException {
        List<PortfolioDTO> list = new ArrayList<>();
        String sql = "select p.asset_id, a.name, p.quantity, p.avg_price"
                + " from portfolio p join asset a on(p.asset_id = a.asset_id)"
                + " where p.user_id = ?"
                + " order by a.name";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    PortfolioDTO dto = new PortfolioDTO();
                    dto.setAssetId(rs.getString("asset_id"));
                    dto.setName(rs.getString("name"));
                    dto.setQuantity(rs.getInt("quantity"));
                    dto.setAvgPrice(rs.getBigDecimal("avg_price"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
}