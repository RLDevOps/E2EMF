<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
      
<table class="table table-striped table-hover table-bordered" id="tb">
    <tr bgcolor="#9acd32">
      <th align="center">Vendor Information:</th>
      <th align="center">Bill To:</th>
       <th align="center">Ship To:</th> 
    </tr>

    <xsl:for-each select="LevelThree/HeaderKey">
    <tr>
      <td><xsl:value-of select="vendor" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="b" /></td>
    </tr>
    </xsl:for-each>
  </table>

<table class="table table-striped table-hover table-bordered" id="tb2">
    <tr bgcolor="#9acd32">
      <th align="center">PO Creation</th>
      <th align="center">Terms:</th>
       <th align="center">Buyer Name:</th>
       <th align="center">PO Change:</th>
      <th align="center">Freight Terms:</th>
      <th align="center">Buyer's Phone/ Fax:</th>
      <th align="center">Payment Term</th>  
        <th align="center">Buyers Email Address:</th>
    </tr>

    <xsl:for-each select="LevelThree/HeaderKey">
    <tr>
      <td><xsl:value-of select="createdon" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="b" /></td>
      <td><xsl:value-of select="c" /></td> 
      <td><xsl:value-of select="d" /></td>
      <td><xsl:value-of select="e" /></td>
      <td><xsl:value-of select="payt" /></td>
      <td><xsl:value-of select="a" /></td>
    </tr>
    </xsl:for-each>
  </table>

<table class="table table-striped table-hover table-bordered" id="tb3">
    <tr bgcolor="#9acd32">
      <th align="center">Line Item</th>
      <th align="center">Material</th>
       <th align="center">Material Description</th>
         <th align="center">Qty</th>
      <th align="center">Uom</th>
         <th align="center">Unit Net Per</th>
      <th align="center">Price Per</th>  
        <th align="center">Total Net Price </th> 
        <th align="center">Total Price with tax</th>
    </tr>

    <xsl:for-each select="LevelThree/HeaderKey">
    <tr>
      <td><xsl:value-of select="litem" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="quantityn" /></td> 
      <td><xsl:value-of select="oun" /></td>
      <td><xsl:value-of select="net" /></td>
      <td><xsl:value-of select="a" /></td>
       <td><xsl:value-of select="netprice" /></td>
      <td><xsl:value-of select="a" /></td>
    </tr>
    </xsl:for-each>
  </table>

<div class="row">
<div class="text-center">Taxes</div>
</div>

<table class="table table-striped table-hover table-bordered" id="tb3">
    <tr bgcolor="#9acd32">
      <th align="center">Spec No.</th>
      <th align="center">Revision No.</th>
       <th align="center">Drawing No.</th>
         <th align="center">Revision No.</th>
    </tr>

    <xsl:for-each select="LevelThree/HeaderKey">
    <tr>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td> 
      <td><xsl:value-of select="a" /></td>
    </tr>
    </xsl:for-each>
  </table>

<table class="table table-striped table-hover table-bordered" id="tb3">
    <tr bgcolor="#9acd32">
      <th align="center">Line N.</th>
      <th align="center">Quantity</th>
      <th align="center">Uom</th>
         <th align="center">Delivery Date</th>

    </tr>

    <xsl:for-each select="LevelThree/HeaderKey">
    <tr>
      <td><xsl:value-of select="litem" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td> 
    </tr>
    </xsl:for-each>
  </table>


<div class="row-fluid">
<div class="text-left col-md-6" style="margin-bottom:20px;color:red">
<strong>Legend: C = Created Item/ M = Modified Item/ D = Deleted Item / Blank = No changes</strong>
</div>
</div>

<table class="table table-striped table-hover table-bordered" id="tb3">
    <tr bgcolor="#9acd32">
      <th align="center">PO Total Values</th>
      <th align="center">Total Net Price</th>
      <th align="center">Totat Price with Taxes</th>
    </tr>

    <xsl:for-each select="LevelThree/HeaderKey">
    <tr>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td>
      <td><xsl:value-of select="a" /></td> 
    </tr>
    </xsl:for-each>
  </table>

<div class="row-fluid">
<div class="text-left col-md-12" style="margin-bottom:20px;color:black">
<strong>Acceptance and fulfillment of this Service/Purchase Order constitutes your acceptance of the Johnson Johnson Services, Inc., Service/Purchase Order.
Terms and Conditions are incorporated herein by reference via thefollowing hyperlink:</strong>
<br />
<p>If you are unable to access the site or if you have any questions, Please contact the Buyer on this Purchase Order.</p>

</div>
</div>

  </body>
  </html>
</xsl:template> 
</xsl:stylesheet> 
