using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

class Program
{
    static void Main()
    {
        string jsonAntes = @"
        {
            ""nome"": ""Bruno"",
            ""contato"": {
                ""email"": ""bruno@gmail.com"",
                ""telefone"": ""(44) 99999-9991""
            },
            ""enderecos"": [
                { ""cidade"": ""Maringá"", ""uf"": ""PR"" }
            ]
        }";

        string jsonDepois = "null";

        var resultado = CompararJson(JToken.Parse(jsonAntes), JToken.Parse(jsonDepois));
        Console.WriteLine(resultado.ToString(Formatting.Indented));
    }

    static JObject CompararJson(JToken antes, JToken depois)
    {
        var resultado = new JObject();
        string caminhoInicial = string.IsNullOrEmpty(antes?.ToString()) ? "raiz" : "";
        CompararToken(antes, depois, resultado, caminhoInicial);
        return resultado;
    }

    static void CompararToken(JToken antes, JToken depois, JObject resultado, string caminho)
    {
        if (antes == null && depois != null)
        {
            AdicionarDiferenca(resultado, "adicionado", caminho, null, depois);
        }
        else if (antes != null && depois == null)
        {
            AdicionarDiferenca(resultado, "removido", caminho, antes, null);
        }
        else if (antes.Type == JTokenType.Object || depois.Type == JTokenType.Object)
        {
            CompararObjetos(antes as JObject ?? new JObject(), depois as JObject ?? new JObject(), resultado, caminho);
        }
        else if (antes.Type == JTokenType.Array || depois.Type == JTokenType.Array)
        {
            CompararArrays(antes as JArray ?? new JArray(), depois as JArray ?? new JArray(), resultado, caminho);
        }
        else if (!JToken.DeepEquals(antes, depois))
        {
            AdicionarDiferenca(resultado, "alterado", caminho, antes, depois);
        }
    }

    static void CompararObjetos(JObject objAntes, JObject objDepois, JObject resultado, string caminho)
    {
        var todasChaves = new HashSet<string>(objAntes.Properties().Select(p => p.Name));
        todasChaves.UnionWith(objDepois.Properties().Select(p => p.Name));

        foreach (var key in todasChaves)
        {
            string caminhoAtual = string.IsNullOrEmpty(caminho) ? key : $"{caminho}.{key}";
            objAntes.TryGetValue(key, out JToken valorAntes);
            objDepois.TryGetValue(key, out JToken valorDepois);
            CompararToken(valorAntes, valorDepois, resultado, caminhoAtual);
        }
    }

    static void CompararArrays(JArray arrayAntes, JArray arrayDepois, JObject resultado, string caminho)
    {
        int tamanho = Math.Max(arrayAntes.Count, arrayDepois.Count);

        for (int i = 0; i < tamanho; i++)
        {
            string caminhoAtual = $"{caminho}[{i}]";
            JToken valorAntes = i < arrayAntes.Count ? arrayAntes[i] : null;
            JToken valorDepois = i < arrayDepois.Count ? arrayDepois[i] : null;
            CompararToken(valorAntes, valorDepois, resultado, caminhoAtual);
        }
    }

    static void AdicionarDiferenca(JObject resultado, string status, string caminho, JToken antes, JToken depois)
    {
        if (string.IsNullOrEmpty(caminho))
            caminho = "raiz";

        if (!resultado.ContainsKey(status))
            resultado[status] = new JObject();

        ((JObject)resultado[status])[caminho] = new JObject
        {
            ["antes"] = antes,
            ["depois"] = depois
        };
    }
}
