package asset.controller;

import asset.dto.AssetDTO;
import asset.service.AssetService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/asset")
public class AssetServlet extends HttpServlet {
    AssetService assetService = new AssetService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<AssetDTO> list = assetService.getAllAssets();
        request.setAttribute("assets", list);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/common/coinresult.jsp");
        rd.forward(request, response);
    }
}