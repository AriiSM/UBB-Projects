namespace SGBD_Lab1
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            FIrma_DGW = new DataGridView();
            label1 = new Label();
            tb_caen = new TextBox();
            tb_nume = new TextBox();
            tb_prenume = new TextBox();
            label2 = new Label();
            label3 = new Label();
            label4 = new Label();
            label5 = new Label();
            label6 = new Label();
            label7 = new Label();
            tb_bonusuri = new TextBox();
            tb_salar = new TextBox();
            tb_functie = new TextBox();
            btn_Update = new Button();
            btn_Delete = new Button();
            Angajati_DGW = new DataGridView();
            label8 = new Label();
            btn_Add = new Button();
            tb_functieFT = new TextBox();
            tb_salarFT = new TextBox();
            tb_bonusuriFT = new TextBox();
            label9 = new Label();
            label10 = new Label();
            label11 = new Label();
            label12 = new Label();
            label13 = new Label();
            tb_prenumeFT = new TextBox();
            tb_numeFT = new TextBox();
            tb_cnpFT = new TextBox();
            label14 = new Label();
            btn_refresh = new Button();
            ((System.ComponentModel.ISupportInitialize)FIrma_DGW).BeginInit();
            ((System.ComponentModel.ISupportInitialize)Angajati_DGW).BeginInit();
            SuspendLayout();
            // 
            // FIrma_DGW
            // 
            FIrma_DGW.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            FIrma_DGW.Location = new Point(12, 79);
            FIrma_DGW.Name = "FIrma_DGW";
            FIrma_DGW.RowHeadersWidth = 51;
            FIrma_DGW.RowTemplate.Height = 29;
            FIrma_DGW.Size = new Size(680, 254);
            FIrma_DGW.TabIndex = 0;
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new Point(240, 39);
            label1.Name = "label1";
            label1.Size = new Size(112, 20);
            label1.TabIndex = 1;
            label1.Text = "Firma Transport";
            // 
            // tb_caen
            // 
            tb_caen.Location = new Point(1167, 421);
            tb_caen.Name = "tb_caen";
            tb_caen.Size = new Size(247, 27);
            tb_caen.TabIndex = 5;
            // 
            // tb_nume
            // 
            tb_nume.Location = new Point(1167, 468);
            tb_nume.Name = "tb_nume";
            tb_nume.Size = new Size(247, 27);
            tb_nume.TabIndex = 6;
            // 
            // tb_prenume
            // 
            tb_prenume.Location = new Point(1167, 520);
            tb_prenume.Name = "tb_prenume";
            tb_prenume.Size = new Size(247, 27);
            tb_prenume.TabIndex = 7;
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Location = new Point(1103, 428);
            label2.Name = "label2";
            label2.Size = new Size(42, 20);
            label2.TabIndex = 8;
            label2.Text = "Caen";
            // 
            // label3
            // 
            label3.AutoSize = true;
            label3.Location = new Point(1096, 475);
            label3.Name = "label3";
            label3.Size = new Size(49, 20);
            label3.TabIndex = 9;
            label3.Text = "Nume";
            // 
            // label4
            // 
            label4.AutoSize = true;
            label4.Location = new Point(1078, 527);
            label4.Name = "label4";
            label4.Size = new Size(67, 20);
            label4.TabIndex = 10;
            label4.Text = "Prenume";
            // 
            // label5
            // 
            label5.AutoSize = true;
            label5.Location = new Point(1089, 576);
            label5.Name = "label5";
            label5.Size = new Size(56, 20);
            label5.TabIndex = 11;
            label5.Text = "Functie";
            // 
            // label6
            // 
            label6.AutoSize = true;
            label6.Location = new Point(1103, 629);
            label6.Name = "label6";
            label6.Size = new Size(42, 20);
            label6.TabIndex = 12;
            label6.Text = "Salar";
            // 
            // label7
            // 
            label7.AutoSize = true;
            label7.Location = new Point(1079, 679);
            label7.Name = "label7";
            label7.Size = new Size(66, 20);
            label7.TabIndex = 13;
            label7.Text = "Bonusuri";
            // 
            // tb_bonusuri
            // 
            tb_bonusuri.Location = new Point(1167, 672);
            tb_bonusuri.Name = "tb_bonusuri";
            tb_bonusuri.Size = new Size(247, 27);
            tb_bonusuri.TabIndex = 14;
            // 
            // tb_salar
            // 
            tb_salar.Location = new Point(1167, 622);
            tb_salar.Name = "tb_salar";
            tb_salar.Size = new Size(247, 27);
            tb_salar.TabIndex = 15;
            // 
            // tb_functie
            // 
            tb_functie.Location = new Point(1167, 569);
            tb_functie.Name = "tb_functie";
            tb_functie.Size = new Size(247, 27);
            tb_functie.TabIndex = 16;
            // 
            // btn_Update
            // 
            btn_Update.Location = new Point(1061, 759);
            btn_Update.Name = "btn_Update";
            btn_Update.Size = new Size(94, 29);
            btn_Update.TabIndex = 17;
            btn_Update.Text = "Update";
            btn_Update.UseVisualStyleBackColor = true;
            btn_Update.Click += btn_Update_Click;
            // 
            // btn_Delete
            // 
            btn_Delete.Location = new Point(1215, 759);
            btn_Delete.Name = "btn_Delete";
            btn_Delete.Size = new Size(94, 29);
            btn_Delete.TabIndex = 18;
            btn_Delete.Text = "Delete";
            btn_Delete.UseVisualStyleBackColor = true;
            btn_Delete.Click += btn_Delete_Click;
            // 
            // Angajati_DGW
            // 
            Angajati_DGW.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            Angajati_DGW.Location = new Point(12, 428);
            Angajati_DGW.Name = "Angajati_DGW";
            Angajati_DGW.RowHeadersWidth = 51;
            Angajati_DGW.RowTemplate.Height = 29;
            Angajati_DGW.Size = new Size(951, 350);
            Angajati_DGW.TabIndex = 19;
            // 
            // label8
            // 
            label8.AutoSize = true;
            label8.Location = new Point(457, 388);
            label8.Name = "label8";
            label8.Size = new Size(65, 20);
            label8.TabIndex = 20;
            label8.Text = "Angajati";
            // 
            // btn_Add
            // 
            btn_Add.Location = new Point(1103, 206);
            btn_Add.Name = "btn_Add";
            btn_Add.Size = new Size(94, 29);
            btn_Add.TabIndex = 21;
            btn_Add.Text = "Add";
            btn_Add.UseVisualStyleBackColor = true;
            btn_Add.Click += btn_Add_Click;
            // 
            // tb_functieFT
            // 
            tb_functieFT.Location = new Point(814, 231);
            tb_functieFT.Name = "tb_functieFT";
            tb_functieFT.Size = new Size(241, 27);
            tb_functieFT.TabIndex = 31;
            // 
            // tb_salarFT
            // 
            tb_salarFT.Location = new Point(814, 284);
            tb_salarFT.Name = "tb_salarFT";
            tb_salarFT.Size = new Size(241, 27);
            tb_salarFT.TabIndex = 30;
            // 
            // tb_bonusuriFT
            // 
            tb_bonusuriFT.Location = new Point(814, 334);
            tb_bonusuriFT.Name = "tb_bonusuriFT";
            tb_bonusuriFT.Size = new Size(241, 27);
            tb_bonusuriFT.TabIndex = 29;
            // 
            // label9
            // 
            label9.AutoSize = true;
            label9.Location = new Point(726, 341);
            label9.Name = "label9";
            label9.Size = new Size(66, 20);
            label9.TabIndex = 28;
            label9.Text = "Bonusuri";
            // 
            // label10
            // 
            label10.AutoSize = true;
            label10.Location = new Point(750, 291);
            label10.Name = "label10";
            label10.Size = new Size(42, 20);
            label10.TabIndex = 27;
            label10.Text = "Salar";
            // 
            // label11
            // 
            label11.AutoSize = true;
            label11.Location = new Point(736, 238);
            label11.Name = "label11";
            label11.Size = new Size(56, 20);
            label11.TabIndex = 26;
            label11.Text = "Functie";
            // 
            // label12
            // 
            label12.AutoSize = true;
            label12.Location = new Point(725, 189);
            label12.Name = "label12";
            label12.Size = new Size(67, 20);
            label12.TabIndex = 25;
            label12.Text = "Prenume";
            // 
            // label13
            // 
            label13.AutoSize = true;
            label13.Location = new Point(743, 137);
            label13.Name = "label13";
            label13.Size = new Size(49, 20);
            label13.TabIndex = 24;
            label13.Text = "Nume";
            // 
            // tb_prenumeFT
            // 
            tb_prenumeFT.Location = new Point(814, 182);
            tb_prenumeFT.Name = "tb_prenumeFT";
            tb_prenumeFT.Size = new Size(241, 27);
            tb_prenumeFT.TabIndex = 23;
            // 
            // tb_numeFT
            // 
            tb_numeFT.Location = new Point(814, 130);
            tb_numeFT.Name = "tb_numeFT";
            tb_numeFT.Size = new Size(241, 27);
            tb_numeFT.TabIndex = 22;
            // 
            // tb_cnpFT
            // 
            tb_cnpFT.Location = new Point(814, 79);
            tb_cnpFT.Name = "tb_cnpFT";
            tb_cnpFT.Size = new Size(241, 27);
            tb_cnpFT.TabIndex = 32;
            // 
            // label14
            // 
            label14.AutoSize = true;
            label14.Location = new Point(757, 86);
            label14.Name = "label14";
            label14.Size = new Size(35, 20);
            label14.TabIndex = 33;
            label14.Text = "Cnp";
            // 
            // btn_refresh
            // 
            btn_refresh.Location = new Point(1249, 25);
            btn_refresh.Name = "btn_refresh";
            btn_refresh.Size = new Size(192, 49);
            btn_refresh.TabIndex = 34;
            btn_refresh.Text = "Refresh";
            btn_refresh.UseVisualStyleBackColor = true;
            btn_refresh.Click += btn_refresh_Click;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1501, 802);
            Controls.Add(btn_refresh);
            Controls.Add(label14);
            Controls.Add(tb_cnpFT);
            Controls.Add(tb_functieFT);
            Controls.Add(tb_salarFT);
            Controls.Add(tb_bonusuriFT);
            Controls.Add(label9);
            Controls.Add(label10);
            Controls.Add(label11);
            Controls.Add(label12);
            Controls.Add(label13);
            Controls.Add(tb_prenumeFT);
            Controls.Add(tb_numeFT);
            Controls.Add(btn_Add);
            Controls.Add(label8);
            Controls.Add(Angajati_DGW);
            Controls.Add(btn_Delete);
            Controls.Add(btn_Update);
            Controls.Add(tb_functie);
            Controls.Add(tb_salar);
            Controls.Add(tb_bonusuri);
            Controls.Add(label7);
            Controls.Add(label6);
            Controls.Add(label5);
            Controls.Add(label4);
            Controls.Add(label3);
            Controls.Add(label2);
            Controls.Add(tb_prenume);
            Controls.Add(tb_nume);
            Controls.Add(tb_caen);
            Controls.Add(label1);
            Controls.Add(FIrma_DGW);
            Name = "Form1";
            Text = "FirmaTransport_Angajati";
            Load += Form1_Load;
            ((System.ComponentModel.ISupportInitialize)FIrma_DGW).EndInit();
            ((System.ComponentModel.ISupportInitialize)Angajati_DGW).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private DataGridView FIrma_DGW;
        private Label label1;
        private TextBox tb_caen;
        private TextBox tb_nume;
        private TextBox tb_prenume;
        private Label label2;
        private Label label3;
        private Label label4;
        private Label label5;
        private Label label6;
        private Label label7;
        private TextBox tb_bonusuri;
        private TextBox tb_salar;
        private TextBox tb_functie;
        private Button btn_Update;
        private Button btn_Delete;
        private DataGridView Angajati_DGW;
        private Label label8;
        private Button btn_Add;
        private TextBox tb_functieFT;
        private TextBox tb_salarFT;
        private TextBox tb_bonusuriFT;
        private Label label9;
        private Label label10;
        private Label label11;
        private Label label12;
        private Label label13;
        private TextBox tb_prenumeFT;
        private TextBox tb_numeFT;
        private TextBox tb_cnpFT;
        private Label label14;
        private Button btn_refresh;
    }
}