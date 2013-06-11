package controllers;

import play.libs.F.Function;
import play.libs.F.Option;
import play.libs.OAuth.OAuthCalculator;
import play.libs.OAuth.RequestToken;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
  
  public static Result index() {
    Option<RequestToken> sessionTokenPair = Twitter.getSessionTokenPair();
    if (sessionTokenPair.isDefined()) {
      return async(WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json")
          .sign(new OAuthCalculator(Twitter.KEY, sessionTokenPair.get()))
          .get()
          .map(new Function<Response, Result>(){
            @Override
            public Result apply(Response result) throws Throwable {
              return ok(result.asJson());
            }
       }));
    }
    return redirect(routes.Twitter.auth());
  }
}