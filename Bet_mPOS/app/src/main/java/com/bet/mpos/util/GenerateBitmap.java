package com.bet.mpos.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.bet.mpos.BetApp;
import com.bet.mpos.R;
import com.bet.mpos.api.pojo.ClientData;
import com.bet.mpos.api.pojo.ReportTransactions;
import com.zoop.pos.plugin.smartpos.requestBuilder.SmartPOSPaymentResponse;
//import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPaymentResponse;

public class GenerateBitmap {

    private String TAG = "GenerateBitmap";

    public static Bitmap drawBetProof(String document, String game, String location, String date) {
        Context context = BetApp.getAppContext();
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        int width = 680;
        int height = 550;
        int y;
        int x = 15;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

//        Bitmap qrcode = Functions.generateQRCode(encrypted);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0xffffffff);

        Rect bounds = new Rect();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setShadowLayer(1f, 0f, 1f, Color.TRANSPARENT);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        paint.setTypeface(typeface);

        Bitmap logo_src;
        logo_src = BitmapFactory.decodeResource(context.getResources(), R.drawable.zape_bet);

        y = logo_src.getHeight();
        canvas.drawBitmap(logo_src, (width - logo_src.getWidth()) / 2, 10, paint);

        paint.setTextSize((int) (18 * scale));
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold2);
        paint.setTypeface(typeface);
        String str = document;
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y+=60, paint);

        paint.setTextSize((int) (18 * scale));
        str = date;
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, width-bounds.width()-x, y, paint);

        //GAME
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_black);
        paint.setTypeface(typeface);

        if(game.length() > 23)
            game = game.substring(0, 22);
        paint.setTextSize((int) (25 * scale));
        str = game;
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width()) / 2, y+=80, paint);

        //LOCATION
        if(location.length() > 30)
            location = location.substring(0, 29);
        paint.setTextSize((int) (18 * scale));
        str = location;
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width()) / 2, y+=60, paint);

        typeface = ResourcesCompat.getFont(context, R.font.montserrat_medium);
        paint.setTypeface(typeface);
        paint.setTextSize((int) (17 * scale));
        str = "Parabéns, sua aposta foi realizada!";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width())/2, y+=60, paint);

        str = "Caso ganhe, você receberá";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width())/2, y+=40, paint);

        str = "O valor na sua conta Pix";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width())/2, y+=40, paint);

        str = "Vinculada ao seu CPF";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width())/2, y+=40, paint);

        str = "De forma automática.";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width())/2, y+=40, paint);

//        canvas.drawBitmap(qrcode, (width - qrcode.getWidth()) / 2, y+=20, paint);
//
//        paint.setTextSize((int) (18 * scale));
//        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold2);
//        paint.setTypeface(typeface);
//        str = code;
//        paint.getTextBounds(str, 0, str.length(), bounds);
//        canvas.drawText(str, (width - bounds.width())/2, y+=(qrcode.getHeight()), paint);

        return bitmap;
    }

    public static Bitmap drawReportSales(ClientData user, ReportTransactions data, String date, String previus_date) {

        Context context = BetApp.getAppContext();

        int soldGrossValue = Integer.parseInt(data.result.debit.get(0).gross) + Integer.parseInt(data.result.credit.get(0).gross) + Integer.parseInt(data.result.pix.get(0).gross);
        int  soldGrossQuantity = data.result.debit.get(0).sales + data.result.credit.get(0).sales + data.result.pix.get(0).sales;
        int  soldLiquidValue = Integer.parseInt(data.result.debit.get(0).net) + Integer.parseInt(data.result.credit.get(0).net) + Integer.parseInt(data.result.pix.get(0).net);
        int  installmentSalesGrossValue = Integer.parseInt(data.result.installments.get(0).gross);
        int  installmentSalesGrossQuantity = data.result.installments.get(0).sales;
        int  creditGross = Integer.parseInt(data.result.credit.get(0).gross);
        int  creditQuantity = data.result.credit.get(0).sales;
        int  debitGross = Integer.parseInt(data.result.debit.get(0).gross);
        int  debitQuantity = data.result.debit.get(0).sales;
        int  pixGross = Integer.parseInt(data.result.pix.get(0).gross);
        int  pixQuantity = data.result.pix.get(0).sales;

        String qtdCredito, qtdDebito, qtdPix;
        String valorTotalCredito, valorTotalDebito, valorTotalPix;
        String CNPJ;
        String NomeFantasia;
        String Endereco, CEP, Cidade, Estado, Pais, Numero;
        String terminal_id;
        String str;
        String minute;

        String qtdCreditoMASTER, qtdCreditoELO, qtdCreditoVISA;
        String valorTotalCreditoMASTER, valorTotalCreditoELO, valorTotalCreditoVISA;
        String qtdDebitoMASTER, qtdDebitoELO, qtdDebitoVISA;
        String valorTotalDebitoMASTER, valorTotalDebitoELO, valorTotalDebitoVISA;

        String loc2, loc1;
        String valorTotal;
        String qtdTotalTransacoes;

        CNPJ = NomeFantasia = Endereco = CEP = Cidade = Estado = Pais = Numero = terminal_id =qtdCreditoMASTER =
                qtdCreditoELO = qtdCreditoVISA =valorTotalCreditoMASTER = valorTotalCreditoELO = valorTotalCreditoVISA =
                        qtdDebitoMASTER = qtdDebitoELO = qtdDebitoVISA = valorTotalDebitoMASTER = valorTotalDebitoELO = valorTotalDebitoVISA =
                                qtdCredito = qtdDebito = qtdPix = valorTotalCredito = valorTotalDebito = valorTotalPix = qtdCreditoMASTER =
                                        qtdCreditoELO = qtdCreditoVISA = valorTotalCreditoMASTER = valorTotalCreditoELO = valorTotalCreditoVISA =
                                                qtdDebitoMASTER = qtdDebitoELO = qtdDebitoVISA = valorTotalDebitoMASTER = valorTotalDebitoELO = valorTotalDebitoVISA =
                                                        qtdTotalTransacoes = valorTotal = "error";


        try {
            if (user.socialName != null)
                NomeFantasia = user.socialName;
            else
                NomeFantasia = user.registerName;

            CNPJ = new MaskPix().maskPix(user.documentValue);
            Endereco = user.addressLine;
            Numero = user.addressNumber;
            CEP = user.addressZipCode.toString();
            Cidade = user.addressCity;
            Estado = user.addressState;
        }catch (Exception e){
            Log.e("drawReport clientData", e.toString());
        }
        Pais = "";
        terminal_id = "Número de série: "+new SerialNumber().getSN();

        int cred_total=0, deb_total=0, pix_total=0;
        int cred_qtde=0, deb_qtde=0, pix_qtde=0;


        cred_total = creditGross;
        cred_qtde = creditQuantity;

        deb_total = debitGross;
        deb_qtde = debitQuantity;

        pix_total = pixGross;
        pix_qtde = pixQuantity;


        valorTotalCredito = Functions.int_to_real(cred_total);
        valorTotalDebito = Functions.int_to_real(deb_total);
        valorTotalPix = Functions.int_to_real(pix_total);
        qtdCredito = cred_qtde+"";
        qtdDebito = deb_qtde+"";
        qtdPix = pix_qtde+"";

        valorTotal = "R$ "+Functions.int_to_real(cred_total+deb_total+pix_total);
        qtdTotalTransacoes = cred_qtde+deb_qtde+pix_qtde+"";

        Bitmap logo_pixcred_src = BitmapFactory.decodeResource(context.getResources(), R.drawable.zape_bet);
        int logo_pixcred_height, nome_fantasia_pos_to_top, data_pos_to_top, endereco_pos_to_top, numero_serie_pos_to_top,
                relatorio_vendas_pos_to_top, todas_vend_pos_to_top, range_data_pos_to_top, x, y, width, height;

        int header_total_pos_to_top, line_header_pos_to_top, endereco2_pos_to_top, credito_pos_to_top;
        int rect_height, rel_title_pos_to_top, line_pos_to_top;

        logo_pixcred_height = logo_pixcred_src.getHeight();
        nome_fantasia_pos_to_top = 55;
        rel_title_pos_to_top = 72;
        line_header_pos_to_top = 25;
        header_total_pos_to_top = 83;
        data_pos_to_top = 35;
        endereco_pos_to_top = 45;
        endereco2_pos_to_top = 35;
        numero_serie_pos_to_top = 35;
        range_data_pos_to_top = 35;
        line_pos_to_top = 40;
        credito_pos_to_top = 56;
        rect_height = line_pos_to_top * 2 + header_total_pos_to_top + credito_pos_to_top + 20;
        todas_vend_pos_to_top = 50;

        width = 680;
        height = logo_pixcred_height
                +data_pos_to_top
                +nome_fantasia_pos_to_top
                +endereco_pos_to_top
                +endereco2_pos_to_top
                +numero_serie_pos_to_top
                +rel_title_pos_to_top
                +todas_vend_pos_to_top
                +range_data_pos_to_top
                +header_total_pos_to_top
                +credito_pos_to_top
                +line_pos_to_top*3;
        x=15;

        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0xffffffff);
        Rect bounds = new Rect();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setShadowLayer(1f, 0f, 1f, Color.TRANSPARENT);


        y=logo_pixcred_height;
        canvas.drawBitmap(logo_pixcred_src, width/2 - logo_pixcred_src.getWidth()/2,0, paint);
        //canvas.drawBitmap(logo_bar_src, 0, logo_pixcred_src.getHeight(), paint);

        //DATA
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        paint.setTypeface(typeface);
        paint.setTextSize((int) (11  * scale));

        paint.getTextBounds(date, 0, date.length(), bounds);
        canvas.drawText(date, width - bounds.width() - 30, y+=data_pos_to_top, paint);//20

        //nome_fantasia_pos_to_top
        //NOME FANTASIA
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold2);
        paint.setTypeface(typeface);
        paint.setTextSize((int) (14  * scale));

        paint.getTextBounds(NomeFantasia, 0 , NomeFantasia.length(), bounds);
        canvas.drawText(NomeFantasia, x+x, y+=nome_fantasia_pos_to_top, paint);

        //CNPJ
        paint.getTextBounds(CNPJ, 0, CNPJ.length(), bounds);
        canvas.drawText(CNPJ, width - bounds.width() - 15 - x, y, paint);

        typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        paint.setTypeface(typeface);
        //line_header_pos_to_top
        //ENDERECO
        loc1 = Endereco+", "+Numero+",";
        paint.getTextBounds(loc1, 0 , loc1.length(), bounds);
        canvas.drawText(loc1, x+x, y+=endereco_pos_to_top, paint);

        loc1 = CEP+", "+Cidade+", "+Estado;
        paint.getTextBounds(loc1, 0 , loc1.length(), bounds);
        canvas.drawText(loc1, x+x, y+=endereco2_pos_to_top, paint);

        //terminal
        paint.getTextBounds(terminal_id, 0 , terminal_id.length(), bounds);
        canvas.drawText(terminal_id, x+x, y+=numero_serie_pos_to_top, paint);

        //rel_title_pos_to_top
        //RELATÓRIO DE VENDAS
        paint.setColor(Color.BLACK);
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold2);
        paint.setTypeface(typeface);
        //str = InternalStorage.read_user_data(context).getData().getUser().getNome_fantasia();
        str = "RELATÓRIO DE VENDAS";
        paint.setTextSize((int) (18 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width()) / 2, y+=rel_title_pos_to_top, paint);//+50

        //line_pos_to_top
        //DESCRICAO
        paint.setColor(Color.BLACK);
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_medium);
        paint.setTypeface(typeface);
        str = "Todas as suas vendas realizadas com sua Smart";
        paint.setTextSize((int) (13 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width()) / 2, y+=todas_vend_pos_to_top, paint);//+50

        //line_pos_to_top
        //PERÍODO
        paint.setColor(Color.BLACK);
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_medium);
        paint.setTypeface(typeface);
//        str = "De " +convert_date(previus_date)+" até "+date;
        str = "De " +previus_date+" até "+date;
        paint.setTextSize((int) (13 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - bounds.width()) / 2, y+=range_data_pos_to_top, paint);//+50

        //RETANGULO
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(new Rect(x,y+rect_height,width-x,y+line_pos_to_top), paint);
        paint.setStyle(Paint.Style.FILL);


        //------------------------------
        //HEADER POS_TO_TOP
        //TOTAL
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold2);
        paint.setTypeface(typeface);
        paint.setTextSize((int) (18  * scale));
        str = "TOTAL";
        paint.getTextBounds(str, 0 , str.length(), bounds);
        canvas.drawText(str, x+x, y+=header_total_pos_to_top, paint);//+64

        canvas.drawRect(new Rect(x,y+15,width-x,y+18), paint);


        //QUANTIDADE TOTAL DE TRANSACOES
        paint.getTextBounds(qtdTotalTransacoes, 0, qtdTotalTransacoes.length(), bounds);
        canvas.drawText(qtdTotalTransacoes, width/2-50 - x, y, paint);

        //VALOR TOTAL TRANSACOES
        paint.getTextBounds(valorTotal, 0, valorTotal.length(), bounds);
        canvas.drawText(valorTotal, width - bounds.width() - 15 - x, y, paint);
        //------------------------------

        //line_pos_to_top
        //CREDITO
        typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        paint.setTypeface(typeface);
        paint.setTextSize((int) (14 * scale));
        str = "CRÉDITO";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x+x, y+=credito_pos_to_top, paint);//+64
        //QTD
        paint.getTextBounds(qtdCredito, 0, qtdCredito.length(), bounds);
        canvas.drawText(qtdCredito, width/2-50 - x, y, paint);
        //TOTAL
        paint.getTextBounds(valorTotalCredito, 0, valorTotalCredito.length(), bounds);
        canvas.drawText(valorTotalCredito, width - bounds.width() - 15 - x, y, paint);


        //line_pos_to_top
        //DÉBITO
        paint.setTextSize((int) (14 * scale));
        str = "DÉBITO";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x+x, y+=line_pos_to_top, paint);//+64
        //QTD
        paint.getTextBounds(qtdDebito, 0, qtdDebito.length(), bounds);
        canvas.drawText(qtdDebito, width/2-50 - x, y, paint);
        //TOTAL
        paint.getTextBounds(valorTotalDebito, 0, valorTotalDebito.length(), bounds);
        canvas.drawText(valorTotalDebito, width - bounds.width() - 15 - x, y, paint);


        //line_pos_to_top
        //CODIGOQR
        paint.setTextSize((int) (14 * scale));
        str = "PIX";
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x+x, y+=line_pos_to_top, paint);//+64
        //QTD
        paint.getTextBounds(qtdPix, 0, qtdPix.length(), bounds);
        canvas.drawText(qtdPix, width/2-50 - x, y, paint);
        //TOTAL
        paint.getTextBounds(valorTotalPix, 0, valorTotalPix.length(), bounds);
        canvas.drawText(valorTotalPix, width - bounds.width() - 15 - x, y, paint);

//        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold);
//        paint.setTypeface(typeface);
//        paint.setTextSize((int) (14  * scale));
//        str = "Subtotal Voucher";
//        paint.getTextBounds(str, 0 , str.length(), bounds);
//        canvas.drawText(str, x+x, y+=header_pos_to_top, paint);//+64
//
//        canvas.drawRect(new Rect(x,y+8,width-x,y+9), paint);

        return bitmap;
    }

    public static Bitmap drawSaleReceipt(SmartPOSPaymentResponse response, String typeSale, String receiptType) {
        Context context = BetApp.getAppContext();

        String sellerName = response.getTransactionData().getSellerName();
        String documentType = response.getTransactionData().getDocumentType();
        String document = response.getTransactionData().getDocument();
        String aid = response.getTransactionData().getAid();
        String arqc = response.getTransactionData().getArqc();
        String auto = response.getTransactionData().getAutoCode();
        String cv = response.getTransactionData().getCv();
        String nsu = response.getTransactionData().getNsu();

        String date = response.getTransactionData().getDate();
        String hour = response.getTransactionData().getHour();

        String status = response.getTransactionData().getStatus();
        int paymentType = response.getTransactionData().getPaymentType();
        int installments = -1;
        int value = response.getTransactionData().getValue();
        String brand = response.getTransactionData().getBrand();
        String pan = response.getTransactionData().getPan();

        String approvalMessage = response.getTransactionData().getApprovalMessage();
        String sellerReceipt = response.getTransactionData().getSellerReceipt();

        // variaveis de controle, de dimensionamento, posicionamento e margens
        int width, height, x, y;
        int margin_top = 40, margin_left = 10, margin_space_between = 50;
        int textSize1 = 20, textSize2 = 14;
        width = 680;
        height = 460;
        x = margin_left;
        y = margin_top;

        if(response.getTransactionData().getInstallments() != null)
        {
            installments = response.getTransactionData().getInstallments();
            height = 500;
        }

        // string reponsavel pelo conteudo que ira escrever no canvas
        String str = "";

        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;

        // cria o bitmap
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        // cria o canvas  a partir do bitmap criado
        Canvas canvas = new Canvas(bitmap);

        // pinta o bitmap de branco
        canvas.drawColor(0xffffffff);

        Rect bounds = new Rect();

        // cria uma tinta
        // ativa antialiasing
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //muda a cor da tinta
        paint.setColor(Color.BLACK);
        paint.setShadowLayer(1f, 0f, 1f, Color.TRANSPARENT);

        // muda a fonte do texto
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat_medium);
        //paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTypeface(typeface);

        // palavra que será escrita
        if(receiptType.equals("customer"))
            str = "VIA CLIENTE"; //str = "VIA ESTABELECIMENTO";
        else
            str = "VIA ESTABELECIMENTO";


        // tamanho da letra
        paint.setTextSize((int) (textSize1 * scale));

        // pega o dimensionamento do texto
        paint.getTextBounds(str, 0, str.length(), bounds);


        // escreve o texto
        canvas.drawText(str, x, y,  paint);
        y += bounds.height() - 10;

        paint.setStrokeWidth(4);
        canvas.drawLine(x, y, width-margin_left, y, paint);
        paint.setStrokeWidth(0);

        y += margin_space_between;

        //NOME
        str = sellerName; //str = "LUKITA DA GALERA";
        paint.setTextSize((int) (textSize1 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);
        y += bounds.height();

        // CPF
        str = documentType + ": " + document; // str = "CPF: " + "42264718811";
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);
        y += bounds.height() + 30;

        // ARQC & AUTO
        str = "ARQC: " + arqc;// str = "ARQC: FCB2E893820488C7";
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);

        str = "AUTO: " + auto; //tr = "AUTO: 685703";
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - margin_left - bounds.width()), y,  paint);

        y += bounds.height() + 8;

        // CV
        str = "CV: " + cv;
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);
        y += bounds.height() + 8;


        // NSU
        str = "NSU: " + nsu;
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);
        y += bounds.height() + 20;

        paint.setStrokeWidth(4);
        canvas.drawLine(x, y, width-margin_left, y, paint);
        paint.setStrokeWidth(0);
        y += margin_space_between;

        // data/hora & valor aprovado
        str = date + " " + hour; //str = "21/06/2023 09:36";
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);


        str = "VALOR APROVADO"; //str = "VALOR APROVADO";
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - margin_left - bounds.width()), y,  paint);

        y += bounds.height() + 14;

        // CREDITO PARCELADO & VALOR
        if(installments != -1)
            str = "CREDITO PARCELADO";
        else
            str = "CREDITO A VISTA";

        paint.setTextSize((int) (textSize1 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);


        str = "R$" + Functions.int_to_real(value);
        paint.setTextSize((int) (textSize1 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - margin_left - bounds.width()), y,  paint);

        y += bounds.height() - 10;

        if(installments != -1)
        {
            // Juros & valor do juros
            if(typeSale.equals("buyer"))
                str = installments + "X COM JUROS";
            else
                str = installments + "X SEM JUROS";

            paint.setTextSize((int) (textSize2 * scale));
            paint.getTextBounds(str, 0, str.length(), bounds);
            canvas.drawText(str, x, y,  paint);

            int valorParcela = value / installments;
            str = "R$" + Functions.int_to_real(valorParcela);
            paint.setTextSize((int) (textSize2 * scale));
            paint.getTextBounds(str, 0, str.length(), bounds);
            canvas.drawText(str, (width - margin_left - bounds.width()), y,  paint);

            y += bounds.height();
        }


        // cartao & AID
        assert pan != null;
        String temp = pan.substring(11);
        str = brand + temp; //str = "MasterCard************0644";
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);

        str = "AID: " + aid;
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (width - margin_left - bounds.width()), y,  paint);

        y += bounds.height() + 8;

        //APROVAÇÃO
        str = approvalMessage;
        paint.setTextSize((int) (textSize2 * scale));
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, x, y,  paint);



        return bitmap;
    }
}
