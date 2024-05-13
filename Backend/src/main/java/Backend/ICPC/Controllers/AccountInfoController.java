package Backend.ICPC.Controllers;

import Backend.ICPC.Models.AccountInfo;
import Backend.ICPC.Models.User;
import Backend.ICPC.Repositories.AccountInfoRepository;
import Backend.ICPC.Repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(value = "Account info controller", description="REST API related to AccountInfo Entity", tags="AccountInfoController")
@RestController
public class AccountInfoController {

    @Autowired
    AccountInfoRepository aIR;

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Get the account info of a specific user.", response = AccountInfo.class, tags = "getAccountInfo")
    @GetMapping(path = {"/accountInfo/{uid}"})
    AccountInfo getAccountInfo(@PathVariable int uid)
    {
        return this.aIR.findById(uid);
    }

    @ApiOperation(value = "Update account info.", response = ResponseEntity.class, tags = "updateAccountInfo")
    @PutMapping(path = {"/accountInfo/{uid}"})
    ResponseEntity<String> updateWebsiteHandle(@Parameter(name = "request", example = "{\ncfHandle : Fefer_Ivan, \n uHandle : felix_halim\n}") @RequestBody HashMap<String, Object> request, @PathVariable int uid)
    {
        AccountInfo accountInfo = this.aIR.findById(uid);
        String cfHandle = (String)(request.get("cfHandle"));
        String uHandle = (String)(request.get("uHandle"));
        if(accountInfo == null)
        {
            AccountInfo ai = new AccountInfo(cfHandle, uHandle);
            ai.setUser(this.userRepository.findById(uid));
            this.aIR.save(ai);
            return ResponseEntity.ok("No existing account info found. New account info created.");
        }
        else
        {
            accountInfo.setCfHandle(cfHandle);
            accountInfo.setuHandle(uHandle);
            this.aIR.save(accountInfo);
            return ResponseEntity.ok("Account info updated.");
        }
    }


}
