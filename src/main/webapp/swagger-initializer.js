window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // Set the default OpenAPI spec URL to your deployed API
  window.ui = SwaggerUIBundle({
    url: "http://localhost:8080/boisson_war_exploded/api/openapi.json",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });

  //</editor-fold>
};
