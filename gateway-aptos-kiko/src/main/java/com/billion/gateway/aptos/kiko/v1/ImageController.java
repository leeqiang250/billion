package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.billion.gateway.aptos.kiko.v1.RequestPathConstant.V1_IMAGE;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({V1_IMAGE})
public class ImageController {

    @GetMapping("/{id}")
    public Response get(HttpServletResponse response, @PathVariable("id") long id) throws IOException {
        response.sendRedirect("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/76360568-5c54-4342-427d-68992ded7b00/public?" + id);
        return Response.success();
    }

}